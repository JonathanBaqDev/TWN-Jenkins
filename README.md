# Build automation and CI/CD 
Reference project: https://gitlab.com/twn-devops-bootcamp/latest/08-jenkins/java-maven-app/-/tree/starting-code

Building a CI/CD pipeline on Jenkins for a Java application.

After following the base setup for Jenkins on the server below, check out info on specific branches linked here:

- [*freestyle-project*](https://github.com/JonathanBaqDev/TWN-Jenkins/blob/freestyle-project/Info/freestyle-project.md) - Jenkins job that checks-out Git repo > Run tests > Builds jar file 
- [*jenkins-docker*](https://github.com/JonathanBaqDev/TWN-Jenkins/blob/jenkins-docker/Info/jenkins-docker.md) - Expanding the freestyle job to Build Docker Image > Push image to Docker Hub and to a private Nexus docker repository
- [*jenkins-pipeline*](https://github.com/JonathanBaqDev/TWN-Jenkins/blob/jenkins-pipeline/Info/jenkins-pipeline.md) - Defining a Jenkinsfile and creating a pipeline vs a freestyle job

### Install Jenkins on a server as a Docker container

* **Configure the server firewall**

  * Add an inbound rule to allow TCP connections on port `8080`.

* **Install Docker**

  * Run `docker` to check whether Docker is installed. If it is not installed, you will see installation options.
  * Update the package index:

    ```bash
    apt update
    ```
  * Install Docker:

    ```bash
    apt install docker.io
    ```

* **Run the Jenkins container**

  ```bash
  docker run -p 8080:8080 -p 50000:50000 -d -v jenkins_home:/var/jenkins_home jenkins/jenkins:lts-jdk21
  ```

  * Port `8080` is used to access the Jenkins web interface.
  * Port `50000` is used for communication between the Jenkins controller and agent nodes.
  * Check the [official Jenkins Docker image repository](https://github.com/jenkinsci/docker) for more information.

* **Access Jenkins**

  * Open the following URL in a browser:

    ```text
    <Server_IP>:8080
    ```

* **Unlock Jenkins**

  * On the initial **Unlock Jenkins** page, retrieve the administrator password from inside the container.
  * You can access the container terminal through **Exec** in Docker Desktop, or by running:

    ```bash
    docker exec -it <container_id> bash
    ```
  * Display the initial administrator password:

    ```bash
    cat <password_file_path>
    ```

* **Access the password through the Docker volume**

  * You can also retrieve the same information by checking where the `jenkins_home` volume is mounted on the host:

    ```bash
    docker volume inspect jenkins_home
    ```

* **Complete the Jenkins setup**

  * Unlock Jenkins using the initial administrator password.
  * Install the recommended plugins.
  * Create the initial administrator user.

## Build tools

Build tools can be made available to Jenkins by either adding plugins or installing directly on the Docker container. 

### Jenkins Tools and Plugins

- The Maven plugin is installed by default 
- Go to *Manage Jenkins* > *Tools*
- Add Maven Installation > Latest version > Install from Apache
- Install Stage View plugin to have a better view of the pipeline build progress - restart the container if the installation seems stuck

### Install on the Jenkins container 

- `docker exec -u 0 -it <container_id> bash` to enter container shell as root.
- `cat /etc/issue` - to confirm what OS distribution you are working with.
- Example of installing NodeJS and npm on the container:
``` 
apt update
apt install curl
curl -sL https://deb.nodesource.com/setup_20.x -o nodesource_setup.sh
bash nodesource_setup.sh
apt install nodejs -y
node -v && npm -v
```