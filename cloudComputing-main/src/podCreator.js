const createPod = async (generatedId,k8sApi) => {
    const pod = {
        apiVersion: 'v1',
        kind: 'Pod',
        metadata: {
          name: 'ffmpeg-pod-' + generatedId,
        },
        spec: {
          restartPolicy: 'OnFailure',
          containers: [
            {
              name: 'ffmpeg-container',
              image: 'jrottenberg/ffmpeg',
              command: ['ffmpeg'],
              args: ['-y', '-i', '/tmp/'+ generatedId + '-input.mp4','-progress','pipe:1' + generatedId, '/tmp/' + generatedId + '-output.mkv'],
              volumeMounts: [
                {
                  name: 'video-pvc',
                  mountPath: '/tmp'
                }
              ]
            }
          ],
          volumes: [
            {
              name: 'video-pvc',
              persistentVolumeClaim: {
                claimName: 'nfs-pvc2'
              }
            }
          ]
        }
      };
    
    await k8sApi.createNamespacedPod('default', pod).then((res) => {
      }).catch((err) => {
        console.log(err);
        return err;
    });
}

module.exports = {
    createPod
}