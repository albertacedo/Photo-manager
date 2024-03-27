

const getStatus = async (id, k8sApi) => {

    //get pod logs
    try {

        let res = await k8sApi.readNamespacedPodLog('ffmpeg-pod-' + id, 'default')



        //parse ffmpeg log to get progress
        data = res.body;

        if (data.includes("statusCode: 404")){
            return {"status":"not found"};
        }
        //if log contains text "response":{"statusCode":400 return queued
        if (data.includes("is waiting to start")) {
            return {"status":"running","progress":0};
        }
        if (data.includes("progress=end")) {
            return {"status":"completed"};
        }


        var tLines = data.toString().split('\n');
        var progress = {};
        let duration = -1;
        for (var i = 0; i < tLines.length; i++) {
            //find Duration
            if (tLines[i].includes("Duration")) {
                durationString = tLines[i].split("Duration: ")[1].split(",")[0];
                //compute duration in seconds
                duration = parseInt(durationString.split(":")[0]) * 3600 + parseInt(durationString.split(":")[1]) * 60 + parseFloat(durationString.split(":")[2]);

            }
            var item = tLines[i].split('=');
            if (typeof item[0] != 'undefined' && typeof item[1] != 'undefined') {
                progress[item[0]] = item[1];
            }
        }

        // The 'progress' variable contains a key value array of the data
        let progressTime = parseInt(progress.out_time.split(":")[0]) * 3600 + parseInt(progress.out_time.split(":")[1]) * 60 + parseFloat(progress.out_time.split(":")[2]);
        return {"status":"running","progress":Math.round(progressTime * 100 / duration)};
    } catch (err) {
        console.log(err)
        if (err.response.statusCode == 404) {
            return {"status":"not found"};
        }
        if (err.response.statusCode == 400) {
            return {"status":"running","progress":0};
        }
    }
}

module.exports = {
    getStatus
}