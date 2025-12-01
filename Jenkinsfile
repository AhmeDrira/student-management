pipeline {
    agent { label 'slave02' }

    environment {
        DOCKERHUB = credentials('dockerhub-creds')
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/AhmeDrira/student-management.git',
                    credentialsId: 'github-token'
            }
        }

        stage('Build Maven') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t ahmedrira/student-management:1.0.0 .'
            }
        }

        stage('Docker Login') {
            steps {
                sh 'echo "$DOCKERHUB_PSW" | docker login -u "$DOCKERHUB_USR" --password-stdin'
            }
        }

        stage('Push Docker Image') {
            steps {
                sh 'docker push ahmedrira/student-management:1.0.0'
            }
        }
    }
}
