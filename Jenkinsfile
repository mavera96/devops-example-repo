pipeline {
    agent any
    
    tools {
        maven 'Maven-3.9'
        jdk 'JDK-17'
    }
    
    environment {
        DOCKER_IMAGE = 'hello-world-app'
        DOCKER_TAG = "${env.BUILD_NUMBER}"
        DOCKER_REGISTRY = 'your-registry.example.com'
        MAVEN_OPTS = '-Xmx1024m'
    }
    
    parameters {
        choice(
            name: 'MAVEN_PROFILE',
            choices: ['dev', 'test', 'prod'],
            description: 'Select Maven/Java profile to build with'
        )
        choice(
            name: 'SPRING_PROFILE',
            choices: ['dev', 'test', 'prod'],
            description: 'Select Spring Boot profile for runtime'
        )
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out source code...'
                checkout scm
            }
        }
        
        stage('Validate') {
            steps {
                echo 'Validating project structure...'
                sh 'mvn validate'
            }
        }
        
        stage('Compile') {
            steps {
                echo "Compiling with Maven profile: ${params.MAVEN_PROFILE}"
                sh "mvn clean compile -P${params.MAVEN_PROFILE}"
            }
        }
        
        stage('Unit Tests') {
            steps {
                echo 'Running unit tests...'
                sh "mvn test -P${params.MAVEN_PROFILE}"
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                    jacoco(
                        execPattern: '**/target/jacoco.exec',
                        classPattern: '**/target/classes',
                        sourcePattern: '**/src/main/java'
                    )
                }
            }
        }
        
        stage('Package') {
            steps {
                echo "Packaging application with profile: ${params.MAVEN_PROFILE}"
                sh "mvn package -DskipTests -P${params.MAVEN_PROFILE}"
            }
            post {
                success {
                    archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
                }
            }
        }
        
        stage('Code Quality Analysis') {
            parallel {
                stage('SonarQube Analysis') {
                    when {
                        expression { return params.MAVEN_PROFILE == 'prod' }
                    }
                    steps {
                        echo 'Running SonarQube analysis...'
                        // Uncomment when SonarQube is configured
                        // sh 'mvn sonar:sonar -Dsonar.host.url=$SONAR_HOST_URL'
                        echo 'SonarQube analysis would run here'
                    }
                }
                
                stage('Dependency Check') {
                    steps {
                        echo 'Checking dependencies for vulnerabilities...'
                        sh 'mvn dependency:tree'
                    }
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    echo "Building Docker image with Spring profile: ${params.SPRING_PROFILE}"
                    sh """
                        docker build \
                            --build-arg SPRING_PROFILE=${params.SPRING_PROFILE} \
                            -t ${DOCKER_IMAGE}:${DOCKER_TAG} \
                            -t ${DOCKER_IMAGE}:latest \
                            .
                    """
                }
            }
        }
        
        stage('Test Docker Image') {
            steps {
                script {
                    echo 'Testing Docker image...'
                    sh """
                        docker run -d --name test-container-${BUILD_NUMBER} \
                            -e SPRING_PROFILES_ACTIVE=${params.SPRING_PROFILE} \
                            -p 8080:8080 \
                            ${DOCKER_IMAGE}:${DOCKER_TAG}
                    """
                    
                    // Wait for container to be ready
                    sh 'sleep 30'
                    
                    // Health check
                    sh """
                        curl -f http://localhost:8080/api/health || exit 1
                        curl -f http://localhost:8080/api/hello || exit 1
                    """
                }
            }
            post {
                always {
                    sh "docker stop test-container-${BUILD_NUMBER} || true"
                    sh "docker rm test-container-${BUILD_NUMBER} || true"
                }
            }
        }
        
        stage('Push to Registry') {
            when {
                branch 'main'
            }
            steps {
                script {
                    echo 'Pushing Docker image to registry...'
                    // Uncomment when registry is configured
                    // sh """
                    //     docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG}
                    //     docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG}
                    // """
                    echo 'Docker push would happen here'
                }
            }
        }
        
        stage('Deploy') {
            when {
                branch 'main'
                expression { return params.MAVEN_PROFILE == 'prod' }
            }
            steps {
                script {
                    echo "Deploying to ${params.MAVEN_PROFILE} environment..."
                    // Add deployment logic here
                    echo 'Deployment would happen here'
                }
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline completed successfully!'
            // Send notification
        }
        failure {
            echo 'Pipeline failed!'
            // Send notification
        }
        always {
            cleanWs()
        }
    }
}
