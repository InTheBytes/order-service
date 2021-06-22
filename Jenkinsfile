pipeline {
    agent any
    tools {
        maven 'Maven'
        jdk 'Java JDK'
        dockerTool 'Docker'
    }
    stages {
        stage('Clean and Test target') {
            steps {
                sh 'mvn clean test'
            }
        }
        stage('Test and Package') {
            steps {
                sh 'mvn package'
            }
        }
        stage('Code Analysis: Sonarqube') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar'
                }
            }
        }
        stage('Await Quality Gateway') {
            steps {
                waitForQualityGate abortPipeline: true
            }
        }
        stage('Dockerize') {
            steps {
                script {
                    docker.build('orderservice')
                }
            }
        }
        stage('Push ECR') {
            steps {
                script {
                    docker.withRegistry('https://241465518750.dkr.ecr.us-east-2.amazonaws.com', 'ecr:us-east-2:aws-ecr-creds') {
                        docker.image('orderservice').push("${env.BUILD_NUMBER}")
                        docker.image('orderservice').push('latest')
                    }
                }
            }
        }
        stage('Deploy') {
            steps {
                echo 'Updating k8s image..'
                sh './kubectl set image deployment/order-service order-service=241465518750.dkr.ecr.us-east-2.amazonaws.com/orderservice:latest'
            }
        }
    }
    post {
        always {
            sh 'mvn clean'
        }
    }
}