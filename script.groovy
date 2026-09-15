#!/usr/bin/env groovy

def test() {
    echo 'running the tests...'
    sh 'mvn test'
}

/* 
* Moved to Jenkins shared library
*
def buildJar() {
    echo 'building the application...'
    sh 'mvn package'
}

def buildImage() {
    echo "building the docker image..."
    withCredentials([
        usernamePassword(
            credentialsId: 'dockerhub-repo', 
            usernameVariable: 'USERNAME', 
            passwordVariable: 'PASSWORD')]) {
                sh 'docker build -t jbaquirindev/twn-demo:jma-2.0 .'
                sh 'echo $PASSWORD | docker login -u $USERNAME --password-stdin'
                sh 'docker push jbaquirindev/twn-demo:jma-2.0'
    }
} 
*/

def deployApp(String imageName) {
    echo 'deploying the application...'

    sshagent(['ec2-server-key']) {
       
        def shellCmd = "bash ./deploy-cmds.sh ${imageName}"
        def remoteHost = "ec2-user@<EC2_INSTANCE_PUBLIC_IP>"
        def copyDestination = "${remoteHost}:/home/ec2-user"

        sh "scp docker-compose.yaml ${copyDestination}"
        sh "scp deploy-cmds.sh ${copyDestination}"
        
        withCredentials([usernamePassword(
            credentialsId: 'dockerhub-repo',
            usernameVariable: 'USERNAME',
            passwordVariable: 'PASSWORD'
        )]) {
            sh """
                set -e
                    printf '%s' "\$PASSWORD" | ssh -o StrictHostKeyChecking=no \\
                        ${remoteHost} \\
                        "docker login --username '\$USERNAME' --password-stdin && ${shellCmd}"
            """
        }
    }
}

return this