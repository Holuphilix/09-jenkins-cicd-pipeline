pipeline {
    agent any
    parameters {
        choice(name: 'DEPLOY_ENV', choices: ['development', 'staging', 'production'], description: 'Select the deployment environment')
    }
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/Holuphilix/09-jenkins-cicd-pipeline.git'
            }
        }
        stage('Build') {
            steps {
                echo "Building application for ${params.DEPLOY_ENV} environment..."
                sh 'docker build -t my-app:latest ./src'
            }
        }
        stage('Test') {
            steps {
                echo "Running automated tests..."
                sh 'python3 -m unittest discover tests'
            }
        }
        stage('Deploy') {
            steps {
                echo "Deploying application to ${params.DEPLOY_ENV} environment..."
                sh """
                docker stop my-app-container-${params.DEPLOY_ENV} || true
                docker rm my-app-container-${params.DEPLOY_ENV} || true
                docker run -d --name my-app-container-${params.DEPLOY_ENV} -p 8080:8080 my-app:latest
                """
            }
        }
    }
    post {
        success {
            echo 'Pipeline executed successfully with tests passed!'
            // Optional: Add Slack or email notification
            // slackSend(channel: '#ci-cd-notifications', message: "Build SUCCESS for ${params.DEPLOY_ENV}")
        }
        failure {
            echo 'Pipeline failed. Check test results and logs.'
            // Optional: Add Slack or email notification
            // slackSend(channel: '#ci-cd-notifications', message: "Build FAILURE for ${params.DEPLOY_ENV}")
        }
        always {
            echo 'Pipeline completed.'
            // Optional: Cleanup actions or archive artifacts
        }
    }
}
