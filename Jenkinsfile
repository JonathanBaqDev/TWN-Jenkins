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
                    BRANCH_NAME == 'multibranch-pipeline'
                }
            }
            steps {
                script {
                    gv.buildJar()
                }
            }
        }

        stage("build image") {
            when {
                expression {
                    BRANCH_NAME == 'multibranch-pipeline'
                }
            }
            steps {
                script {
                    gv.buildImage()
                }
            }
        }

        stage("deploy") {
            when {
                expression {
                    BRANCH_NAME == 'multibranch-pipeline'
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