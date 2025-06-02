pipeline {
    agent {
        dockerfile {
            filename 'agent/Dockerfile'
        }
    }

    environment {
        DOCKERHUB_AUTH = credentials('DockerHubCredentials')
        HOSTNAME_DEPLOY_STAGING = "192.168.42.3"
        // Liste des services à construire et déployer
         SERVICES = "discovery-service,gateway,user-service,comment-service,notification-service,post-service,media-service-app"
    }

    stages {
        stage('Test') {
            steps {
                // Exécution des tests pour chaque service
                script {
                    env.SERVICES.split(',').each { service ->
                        if (fileExists("${service.trim()}/pom.xml")) {
                            dir(service.trim()) {
                                sh 'mvn clean test'
                            }
                        }
                    }
                }
            }

            post {
                always {
                    // Collecte des rapports de test pour tous les services
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                script {
                    env.SERVICES.split(',').each { service ->
                        if (fileExists("${service.trim()}/pom.xml")) {
                            dir(service.trim()) {
                                sh 'mvn clean package -DskipTests'
                            }
                        }
                    }
                }
            }
        }

        stage('Prepare deployment files') {
            steps {
                script {
                    // Création d'un répertoire de déploiement temporaire
                    sh 'rm -rf inter-deploy && mkdir -p inter-deploy'
                    
                    // Copie du docker-compose.yml
                    sh 'cp ./docker-compose.yml inter-deploy/'

                }
            }
        }

        stage('Deploy in staging') {
            when {
                expression { GIT_BRANCH == 'origin/main' }
            }
            steps {
                sshagent(credentials: ['SSH_AUTH_SERVER']) { 
                    sh """
                        [ -d ~/.ssh ] || mkdir ~/.ssh && chmod 0700 ~/.ssh
                        ssh-keyscan -t rsa,dsa ${HOSTNAME_DEPLOY_STAGING} >> ~/.ssh/known_hosts
                        
                        # Création du répertoire de déploiement sur le serveur distant
                        ssh zero@${HOSTNAME_DEPLOY_STAGING} 'mkdir -p /home/zero/inter-deploy'
                        
                        # Copie des fichiers de déploiement
                        scp -r ./* zero@${HOSTNAME_DEPLOY_STAGING}:/home/zero/inter-deploy/
                        
                        # Exécution des commandes de déploiement
                        ssh -t zero@${HOSTNAME_DEPLOY_STAGING} '
                            cd /home/zero/inter-deploy &&
                            docker compose down &&
                            docker compose up -d --build
                        '
                    """
                }
            }
        }

        stage('Test Staging') {
            when {
                expression { GIT_BRANCH == 'origin/main' }
            }
            steps {
                script {
                    // Vérification que les services sont opérationnels
                    def endpoints = [
                        'discovery-service': '8761',
                        'gateway': '8888',
                        'user-service': '8082',
                        'comment-service': '8078',
                        'notification-service': '8083',
                        'post-service': '8081',
                        'media-service-app': '8000'
                    ]
                    
                    endpoints.each { service, port ->
                        sh """
                            sleep 30
                            apk add --no-cache curl
                            curl -I http://${HOSTNAME_DEPLOY_STAGING}:${port} || true
                        """
                    }
                }
            }
        }
    }
}