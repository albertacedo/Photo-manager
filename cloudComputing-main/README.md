# Deploying a Node App to Google Cloud with Kubernetes

## Want to learn how to build this?

Check out the [post](https://testdriven.io/deploying-a-node-app-to-google-cloud-with-kubernetes).

## Want to use this project?

### Docker

Build the images and spin up the containers:

```sh
$ docker-compose up -d --build
```

Run the migrations and seed the database:

```sh
$ docker-compose exec web knex migrate:latest
$ docker-compose exec web knex seed:run
```

### Kubernetes

#### Google Cloud Platform (GCP)

Install the [Google Cloud SDK](https://cloud.google.com/sdk), run `gcloud init` to configure it, and then either pick an existing GCP project or create a new project to work with.

Set the project:

```sh
$ gcloud config set project <PROJECT_ID>
```

Install `kubectl`:

```sh
$ gcloud components install kubectl
```

#### Kubernetes Cluster

Create a cluster on [Kubernetes Engine](https://console.cloud.google.com/kubernetes):

```sh
$ gcloud container clusters create node-kubernetes \
    --num-nodes=3 --zone us-central1-a --machine-type g1-small
```

Connect the `kubectl` client to the cluster:

```sh
$ gcloud container clusters get-credentials node-kubernetes --zone us-central1-a
```

#### Docker

Build and push the image to the [Container Registry](https://cloud.google.com/container-registry/):

```sh
$ gcloud auth configure-docker
$ docker build -t gcr.io/<PROJECT_ID>/node-kubernetes:v0.0.1 .
$ docker push gcr.io/<PROJECT_ID>/node-kubernetes:v0.0.1
```

```

#### Volume

Create a [Persistent Disk](https://cloud.google.com/persistent-disk/):

```sh
gcloud compute disks create --size=10GB --zone=europe-west1-b nfs-disk

```
Create NFS server
```sh
kubectl create -f ./kubernetes/nfs-server-deployment.yaml
```

Create cluster ip
```sh
kubectl create -f ./kubernetes/nfs-clusterip-service.yaml
```

Create the volume:

```sh
kubectl create -f ./kubernetes/nfs-pv-pvc.yaml
```


#### Node

Create the deployment:

```sh
$ kubectl create -f ./kubernetes/node-deployment.yaml
```

Create the service:

```sh
$ kubectl create -f ./kubernetes/node-service.yaml
```


Grab the external IP:

```sh
$ kubectl get service node

NAME      TYPE           CLUSTER-IP      EXTERNAL-IP     PORT(S)          AGE
node      LoadBalancer   10.39.244.136   35.232.249.48   3000:30743/TCP   2m
```

Test it out:

1. [http://EXTERNAL_IP:3000](http://EXTERNAL_IP:3000)
1. [http://EXTERNAL_IP:3000/todos](http://EXTERNAL_IP:3000/todos)

#### Remove

Remove the resources once done:

TODO
# cloudComputing
"# cloudComputing" 
