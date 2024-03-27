/*
  imports
*/


const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const path = require('path');

const app = express();
const PORT = process.env.PORT || 3000;
const fs = require('fs');

var multer = require('multer');
var upload = multer();

const k8s = require('@kubernetes/client-node');
const kc = new k8s.KubeConfig();
kc.loadFromDefault();
const k8sApi = kc.makeApiClient(k8s.CoreV1Api);

const { v4: uuidv4 } = require('uuid');
const { REFUSED } = require('dns');

const {  getStatus } = require('./status');
const { createPod } = require('./podCreator');
const { jobChecker } = require('./jobChecker');

/*
  middleware
*/

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

app.use(cors());
/*
  routes
*/

const db = require('./db');
 db.init();
 db.executeQuery('SELECT NOW()').then((res) => {

  console.log(res);
});

setInterval(() => {
  jobChecker(k8sApi);
}, 1000);

app.get('/', (req, res) => {
  //html with file input
  res.sendFile(__dirname + '/index.html');
});

app.post('/upload',upload.single('video'), async (req, res) => {
  let generatedId = uuidv4();
  
  fs.writeFileSync('/tmp/'+ generatedId + '-input.mp4', req.file.buffer);

  let numberOfRunningJobs = await db.executeQuery('SELECT COUNT(*)::text FROM JOB WHERE status = $1', ['running']);
  numberOfRunningJobs = parseInt(numberOfRunningJobs[0].count);
  if (numberOfRunningJobs >= 2){
    let res = await db.executeQuery('INSERT INTO JOB(id, filename, status,creation) VALUES ($1, $2, $3, $4)', [generatedId, req.file.originalname, 'queued', new Date()])
    res.send(generatedId);
    return;
  }
  //insert into db
  await db.executeQuery('INSERT INTO JOB(id, filename, status,creation) VALUES ($1, $2, $3, $4)', [generatedId, req.file.originalname, 'running', new Date()])
 
  //create a pod
  await createPod(generatedId,k8sApi);
  
  res.send(generatedId);
});




app.get("/status/:id", async (req, response) => {
  //get status from db
  let result = await db.executeQuery('SELECT status FROM JOB WHERE id = $1', [req.params.id]);
  console.log(result);
  if (result.length == 0){
    response.json({status: 'not found'});
    return;
  }
  let status = result[0].status;
  if ( status == 'completed'){
    response.json({status: 'completed'});
    return;
  }
  if (status == 'queued'){
    let position = await db.executeQuery('SELECT COUNT(*)::text FROM JOB WHERE status = $1 AND creation < (SELECT creation FROM JOB WHERE id = $2)', ['queued', req.params.id])[0].count;
    response.json({status: 'queued', position: position});
    return;
  }
  response.json(await getStatus(req.params.id,k8sApi))


});
app.get("/video/:id", (req, response) => {
  //send video store in /tmp/id-output.mkv
  //find file in /tmp by name 
  response.sendFile('/tmp/'+findByName('/tmp', req.params.id + '-output'));
});

const findByName =  (dir, name) => {
  const matchedFiles = [];

  const files = fs.readdirSync(dir);

  for (const file of files) {
      const filename = path.parse(file).name;

      if (filename === name) {
          matchedFiles.push(file);
      }
  }

  return matchedFiles;
};

app.get("/nodes", (req, res) => {
  const k8s = require('@kubernetes/client-node');
  const kc = new k8s.KubeConfig();
  kc.loadFromDefault();
  const k8sApi = kc.makeApiClient(k8s.CoreV1Api);
  k8sApi.listNode().then((res) => {
    console.log(res.body);
    res.json(res.body);
  }).catch((err) => {
    console.log(err);
    res.json(err);
  });
});

app.get("/healthz", (req, res) => {
  res.send("ok")
});

/*
  run server
*/

app.get("/debug/:pod",(req, res) => {
  k8sApi.readNamespacedPodLog(req.params.pod, 'default').then((response) => {
    res.json(response.body);
  }).catch((err) => {
    res.json(err);
  });
})

app.listen(PORT, () => {
  console.log(`Listening on port: ${PORT}`);
});
