
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
                    env.IMAGE_NAME = "$version-$BUILD_NUMBER"
                }
            }
        }
        
        stage("build jar") {
            when {
                expression {
                    BRANCH_NAME == 'multibranch-versioning' 
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
                    BRANCH_NAME == 'multibranch-versioning' 
                }
            }
            steps {
                script {
                    def dockerRepo = 'jbaquirindev/twn-demo'
                    def imageName = "${dockerRepo}:${env.IMAGE_NAME}"

                    buildImage(imageName)
                    dockerLogin()
                    dockerPush(imageName)
                }
            }
        }

        stage("deploy") {
            when {
                expression {
                    BRANCH_NAME == 'multibranch-versioning'
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