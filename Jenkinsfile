pipeline {
    agent { label 'slave01' }   // agent Jenkins où Maven + Docker sont installés

    environment {
        DOCKER_IMAGE = "ahmedrira/student-management"
        DOCKER_TAG   = "1.0.0"

        // ID des credentials Docker Hub dans Jenkins
        DOCKERHUB = credentials('dockerhub-creds')
    }

    stages {
        stage('Checkout') {
            steps {
                // récupère le code du repo configuré dans Jenkins (SCM)
                checkout scm
            }
        }

        stage('Maven build (skip tests)') {
            steps {
                sh 'mvn -B clean package -DskipTests'
            }
        }

        stage('Docker build') {
            steps {
                sh 'docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .'
            }
        }

        stage('Docker push') {
            steps {
                sh '''
                  docker push ${DOCKER_IMAGE}:${DOCKER_TAG}
                  docker logout
                '''
            }
        }
    }
}
