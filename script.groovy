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

def deployApp() {
    echo 'deploying the application...'
    sshagent(['ec2-server-key']) {
        def imageName = 'jbaquirindev/twn-demo:1.1.2-8'
        def dockerRun = "docker run -d -p 8080:8080 ${imageName}"
        
        withCredentials([usernamePassword(
            credentialsId: 'dockerhub-repo',
            usernameVariable: 'USERNAME',
            passwordVariable: 'PASSWORD'
        )]) {
            sh """
                set -e
                    printf '%s' "\$PASSWORD" | ssh -o StrictHostKeyChecking=no \\
                        ec2-user@<EC2_INSTANCE_PUBLIC_IP> \\
                        "docker login --username '\$USERNAME' --password-stdin && docker pull ${imageName} && ${dockerRun}"
            """
        }
    }
}

return this