### Testing your K8s deployment locally

#### Use Minikube

Links:
* https://minikube.sigs.k8s.io/docs/start/
* https://docs.docker.com/engine/install/ubuntu/

1. Make sure your computer (or VM) has enough disk space.

   I was doing this on a Linux VirtualBox with disk=25G, ram=4G, cpu=2.

1. Install docker if you haven't already. (see link)

1. (Linux only I think) Make sure you can run docker as non-superuser.
   ```shell
   $ sudo usermod -aG docker $USER && newgrp docker
   ```
1. If you're using a Mac then you should consider installing a hypervisor.
   ```shell
    $ brew update
    $ brew install hyperkit
   ```

1. I don't know what this is but do it (or don't).
   ```shell
   $ sudo apt-get install -y conntrack
   ```

1. Install and run Minikube (see link)
   ```shell
   $ minikube start
   ```

1. Enable the ingress addon for Minikube.

   ```shell
   $ minikube addons enable ingress
   ```

#### Build and deploy a container image with Docker

```shell
$ docker build --tag voteservice .

$ docker run -dp 8080:8080 voteservice
$ docker ps
$ docker stop [name_from_ps]
$ docker ps -a # see terminated processes
$ docker restart [name_from_ps]
$ docker remove [name_from_ps]

# push to docker hub
$ docker tag voteservice macdoogles/voteservice:0.0.1
$ docker push macdoogles/voteservice:0.0.1

# run it on any other machine
$ docker run -dp 8080:8080 macdoogles/voteservice:0.0.1
$ docker ps
$ docker run -dp 8080:8080 --name voteservice macdoogles/voteservice
$ docker kill voteservice
```

There will be issues if you build on Mac and run on Linux. There's a flag to compile for amd64 
but it takes a _really_ long time on an M2 processor. I just built my docker images on Linux.

#### Starting the service with Minikube

1. Important: please remember to have added the Ingress addon to Minikube.
   ```shell
   $ minikube addons enable ingress
   ```

1. Optional: Set a namespace.

   To set a namespace for different environments, see
   https://kubernetes.io/docs/tasks/administer-cluster/namespaces-walkthrough/

   ```shell
   $ kubectl config view
   $ kubectl create namespace voteservice-dev
   $ kubectl config set-context dev --namespace=voteservice-dev --cluster=minikube --user=minikube
   $ kubectl config use-context dev
   ```

1. Create a secret for the connection string. 

   This is using MongoDB Atlas for now so log into that place and get the connection string with the
   password included. There's a few options from there but I usually base64 encode it and execute a 
   yaml file like this: `$ kubectl apply -f k8s/secret.yaml`. Please see
    [secrets/README.md](secrets/README.md).

1. Apply the configmap changes.
   ```shell
   $ kubectl apply -f k8s/configs/dev.yaml
   ```

1. Apply everything else.
   ```shell
   $ kubectl apply -f k8s/voteservice.yaml
   ```
   
1. Do more Minikube things.
   ```shell
   $ minikube service voteapi-service
   ```

1. Get the port number and update hosts.

   ```shell
   $ kubectl get ingress [-n namespace]
   $ # Using the port under the ADDRESS column from the previous command
   $ sudo echo '192.168.49.2    agaber.dev' >> /etc/hosts
   ```

At that point these links should at least be working on the computer where you're running Minikube.

* http://agaber.dev/vote/api/v1/elections
* http://192.168.49.2/healthz

You can stop and start over with minikube like this:

```shell
$ minikube stop
$ minikube delete
$ minikube start
```
