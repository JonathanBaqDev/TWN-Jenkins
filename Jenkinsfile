
// Import global library configured in Jenkins
//@Library('jenkins-shared-library')

// Import to be project scoped vs global
library identifier: 'jenkins-shared-library@main', retriever: modernSCM(
    [$class: 'GitSCMSource',
        remote: 'https://github.com/JonathanBaqDev/TWN-Jenkins-Shared-Library.git',
        credentialsId: 'github-creds'
    ]
)

def gv

pipeline {   
    agent any

    environment {
        IMAGE_TAG = ''
        DOCKERHUB_REPO = 'jbaquirindev/twn-demo'
        IMAGE_NAME = ''
    }

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
        stage ("increment version") {
            steps {
                script {
                    echo "Incrementing app version"
                    sh 'mvn build-helper:parse-version versions:set \
                        -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
                        versions:commit'
                    def versionMatcher = readFile('pom.xml') =~ '<version>(.+)</version>'
                    def version = versionMatcher[0][1]
                    // BUILD_NUMBER is a Jenkins environment variable that increments with each build
                    env.IMAGE_TAG = "$version-$BUILD_NUMBER"
                    env.IMAGE_NAME = "${env.DOCKERHUB_REPO}:${env.IMAGE_TAG}"
                }
            }
        }
        
        stage("build jar") {
            when {
                expression {
                    BRANCH_NAME == 'multibranch-AWS' 
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
                    BRANCH_NAME == 'multibranch-AWS' 
                }
            }
            steps {
                script {
                    def imageName = env.IMAGE_NAME

                    buildImage(imageName)
                    dockerLogin()
                    dockerPush(imageName)
                }
            }
        }

        stage("deploy") {
            when {
                expression {
                    BRANCH_NAME == 'multibranch-AWS'
                }
            }
            steps {
                script {
                    gv.deployApp(env.IMAGE_NAME)
                }
            }
        }

        stage('commit version update') {
            when {
                expression {
                    BRANCH_NAME == 'multibranch-AWS'
                }
            }
            steps {
                script {
                    gitPush('multibranch-AWS', 'github.com/JonathanBaqDev/TWN-Jenkins.git')
                }
            }
        }               
    }
} 