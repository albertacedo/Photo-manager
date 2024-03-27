const db = require('./db');
const { createPod} = require('./podCreator');
const { getStatus } = require('./status');
const MaxRunningJobs = 2;
const jobChecker = async (k8sApi) => {
    //console.log("checking jobs");
    let runningJobsIds = await db.executeQuery('SELECT id::text FROM JOB WHERE status = $1', ['running'])

    let totalRunning = 0;
    for (let i = 0; i < runningJobsIds.length; i++) {
        let id = runningJobsIds[i].id;
        let response = await getStatus(id,k8sApi);
        console.log(response);
        let status = response.status;
        if (status == "not found"){
            createPod(id,k8sApi);
            continue;
        }
        if (status == "completed"){
            await db.executeQuery('UPDATE JOB SET status = $1 WHERE id = $2', ['completed', id]);
            continue;
        } else {
            totalRunning++;
        }
    }

    if(totalRunning < MaxRunningJobs){
        let queuedJobs =await db.executeQuery('SELECT id::text FROM JOB WHERE status = $1 ORDER BY creation ASC', ['queued']);
        let numberOfJobsToRun = MaxRunningJobs - totalRunning > queuedJobs.length ? queuedJobs.length : MaxRunningJobs - totalRunning;


        for(let i = 0; i < numberOfJobsToRun ; i++){
            let id = queuedJobs[i].id;
            console.log("running job " + id);
            await db.executeQuery('UPDATE JOB SET status = $1 WHERE id = $2', ['running', id]);
            createPod(id,k8sApi);
        }
    }
}

module.exports = {
    jobChecker
}
