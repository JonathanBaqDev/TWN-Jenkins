def test() {
    echo 'running the tests...'
    sh 'mvn test'
}

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

def deployApp() {
    echo 'deploying the application...'
}

return this