## Configuring Docker in Jenkins

### Mount Docker runtime

To make Docker available on Jenkins, mount the Docker runtime on the host server to the Jenkins container as a volume

- Stop the current container - `docker stop <container_id>` 
- We already mounted a volume for Jenkins initially (jenkins_home) so all app data can be attached to the new container we will run, check with `docker volume ls`
- Run the new container by running:
```
docker run -p 8080:8080 -p 50000:50000 -d \
-v jenkins_home:/var/jenkins_home \
-v /var/run/docker.sock:/var/run/docker.sock \
jenkins/jenkins:lts-jdk21
```
- Acess the container shell as root `docker exec -u 0 -it <container_id> bash `
- Fetch the latest version of docker, set permissions and install
```
curl https://get.docker.com/ > dockerinstall && chmod +x dockerinstall && ./dockerinstall
```
- Set read-write permissions for all users on the mounted Docker socket:
```
chmod 666 /var/run/docker.sock
```
- Verify Docker access as the Jenkins user:
```
docker exec -u jenkins <container_id> docker version
```
- If Docker recreates `/var/run/docker.sock` after a daemon restart, run the `chmod` command again. Note that access to this socket effectively grants root-level control of the Docker host, so use this approach only on a trusted development or lab machine.

### Build application Docker image in Jenkins

- Add a Docker file (see [commit](https://github.com/JonathanBaqDev/TWN-Jenkins/commit/04cc0e5ae56f7d6cfaeed0e8f7e3fa22a740ad83))
- In Jenkins, configure the freestyle job we created (see branch *freestyle-project*)
- Point to the correct branch where you pushed the Dockerfile
- Add a build step > Execute Shell `docker build -t java-maven-app:1.0 .`
- Check the build output and confirm in the Docker container `docker image ls`, you should see the image for *java-maven-app:1.0*

### Push Image to Docker Hub private repository

- Create an account in DockerHub 
- In DockerHub, create a private repository
- In Jenkins, Settings > Credentials > Add credential
- Select Username with password > Add your DockerHub account credentials
- On the freestyle job, Configure > Environment > Use secret
- Add Username and password (Separated) binding
- Set variable names like USERNAME and PASSWORD accordingly,select your Dockerhub credentials
- Under the shell command, edit the image tag to be like below - you can find this info on the Dockerhub repo under "Docker commands". Then push the image to Dockerhub:
```
docker build -t <dockerhub_username>/<repo_name>:jma-1.0 .
echo $PASSWORD | docker login -u $USERNAME --password-stdin 
docker push <dockerhub_username>/<repo_name>:jma-1.0
```
- Save and build - confirm the app image was pushed to your Dockerhub repository

### Push Image to a private Nexus repository

- Check the [TWN-Nexus-Gradle](https://github.com/JonathanBaqDev/TWN-Nexus-Gradle) repo for more info on setting up Nexus
- Check the [TWN-Docker](https://github.com/JonathanBaqDev/TWN-Docker) repo, *nexus-publish* and *nexus-deploy* branches for how to create a docker repo and configure users and credentials to publish images
- Start a Nexus Docker container:
```
docker volume create --name nexus-data
docker run -d -p 8081:8081 --name nexus -v nexus-data:/nexus-data sonatype/nexus3
```
- Since Nexus is running on http in our example, configure insecure communication for it to be allowed on the Jenkins host server: `vim /etc/docker/daemon.json`
- Add the insecure registries configuration
```
{
    "insecure-registries":["<nexus_server_IP>:<nexus-docker_repo_port>"]
}
```
- Restart Docker: `systemctl restart docker`
- Re-run `chmod 666 /var/run/docker.sock` as root on the Jenkins container (See *mount docker runtime* instructions above)
- On Jekins, create credentials for Nexus
- Configure the freestyle job > Select Nexus credentials in Bindings
- Edit the shell build command
```
docker build -t <nexus_server_IP>:<nexus_docker_repo_port>/java-maven-app:1.0 .
echo $PASSWORD | docker login -u $USERNAME --password-stdin <nexus_server_IP>:<nexus_docker_repo_port>
docker push <nexus_server_IP>:<nexus_docker_repo_port>/java-maven-app:1.0
```
- Build and check console output
- Confirm image has been pushed to the Docker repo in Nexus