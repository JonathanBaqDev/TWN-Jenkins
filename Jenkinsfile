@Library('jenkins-shared-library')
def gv

pipeline {   
    agent any
    tools {
        maven 'maven-3.9'
    }
    stages {
        stage("init") {
            steps {
                script {
                    gv = load "script.groovy"
                    echo "Script loaded, building the application from branch ${BRANCH_NAME}"
                }
            }
        }
        stage ("test") {
            steps {
                script {
                    gv.test()
                }
            }
        }
        stage("build jar") {
            when {
                expression {
                    BRANCH_NAME == 'multibranch-sharedlib' 
                }
            }
            steps {
                script {
                    buildJar()
                }
            }
        }

        stage("build and push image") {
            when {
                expression {
                    BRANCH_NAME == 'multibranch-sharedlib' 
                }
            }
            steps {
                script {
                    def imageName = 'jbaquirindev/twn-demo:jma-3.0'

                    buildImage(imageName)
                    dockerLogin()
                    dockerPush(imageName)
                }
            }
        }

        stage("deploy") {
            when {
                expression {
                    BRANCH_NAME == 'multibranch-sharedlib'
                }
            }
            steps {
                script {
                    gv.deployApp()
                }
            }
        }               
    }
} 