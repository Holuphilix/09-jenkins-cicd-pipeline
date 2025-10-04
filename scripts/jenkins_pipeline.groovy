pipeline {
    agent any
    environment {
        APP_NAME = 'my-app'
        DEPLOY_ENV = 'development'
    }
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/Holuphilix/09-jenkins-cicd-pipeline.git'
            }
        }
        stage('Build') {
            steps {
                echo 'Building Docker image...'
                sh 'docker build -t my-app:latest ./src'
            }
        }
        stage('Test') {
            steps {
                echo 'Running tests inside Docker container...'
                sh 'docker run --rm my-app:latest /bin/sh -c "echo Testing application"'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying Docker container...'
                sh 'docker run -d --name my-app-container -p 8080:8080 my-app:latest'
            }
        }
    }
    post {
        success {
            echo 'Pipeline executed successfully with Docker!'
        }
        failure {
            echo 'Pipeline failed. Check Docker logs.'
        }
    }
}
