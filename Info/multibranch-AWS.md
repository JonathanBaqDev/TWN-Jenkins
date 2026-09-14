## Deploy app from Jenkins to EC2

### Configure Jenkins
- Install the *SSH Agent* plugin.
- Create credentials using your EC2 key pair's private key.
- In your multibranch pipeline, go to **Credentials** (project-scoped).
- Create a new *SSH Username with Private Key* credential.
- Set the username to *ec2-user*.
- Paste the contents of the private key (PEM) file directly into the key section.
- In the multibranch pipeline, go to **Pipeline Syntax** > **Snippet Generator** > **Steps**.
- Under **Sample Step**, select *sshagent* > *ec2-user* > **Generate Pipeline Script**.
- Copy the generated syntax into your Jenkinsfile.

### Jenkinsfile
- See the changes in this [commit](https://github.com/JonathanBaqDev/TWN-Jenkins/commit/26450946cb2c29be2b23ac457a75627df7d4bb81) for an example of how to SSH into the EC2 instance, log in to the Docker Hub repository, pull the application image, and deploy the container.
- Allow access to EC2 from Jenkins: **EC2** > **Security** > **Security Groups**.
- Edit the inbound rules to allow TCP/22 from the Jenkins IP address.
- Allow TCP/3080 (the application port) from all sources.