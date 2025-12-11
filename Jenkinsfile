pipeline {
    agent { label 'slave02' }

    environment {
        // Docker Hub (username + password)
        DOCKERHUB = credentials('dockerhub-creds')

        // Token SonarQube (Secret text dans Jenkins)
        SONAR_TOKEN = credentials('sonar-token')

        // URL du serveur SonarQube (ton conteneur Docker)
        SONAR_HOST_URL = 'http://192.168.33.10:9000'
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

        stage('SonarQube Analysis') {
            steps {
                sh '''
                  mvn sonar:sonar \
                    -Dsonar.projectKey=student-management \
                    -Dsonar.host.url=$SONAR_HOST_URL \
                    -Dsonar.login=$SONAR_TOKEN
                '''
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
	
	stage('Kubernetes Deploy') {
            steps {
                sh '''
                  echo "Déploiement MySQL sur Kubernetes..."
                  kubectl apply -n devops -f k8s/devops/mysql-deployment.yaml

                  echo "Déploiement Spring Boot sur Kubernetes..."
                  kubectl apply -n devops -f k8s/devops/student-management-deployment.yaml

                  echo "Ressources dans le namespace devops :"
                  kubectl get pods,svc -n devops
                '''
            }
        }

    }	
}
