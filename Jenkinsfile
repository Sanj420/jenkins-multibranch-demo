pipeline {
    agent {
        label 'jenkins-agent'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Install Java 21 JDK') {
    steps {
        sh '''
            sudo apt-get update
            sudo apt-get install -y openjdk-21-jdk

            export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
            export PATH=$JAVA_HOME/bin:$PATH

            java -version
            javac -version
        '''
    }
}

        stage('Build') {
            steps {
                sh '''
                    export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
                    export PATH=$JAVA_HOME/bin:$PATH

                    ./mvnw clean package -DskipTests
                '''
            }
        }

        stage('Test') {
            steps {
                sh '''
                    export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
                    export PATH=$JAVA_HOME/bin:$PATH

                    ./mvnw test
                '''
            }
        }

stage('SonarQube Analysis') {
    steps {
        withSonarQubeEnv('SonarQubeDemo') {
            withCredentials([
                string(
                    credentialsId: 'sonarqube-token',
                    variable: 'SONAR_AUTH_TOKEN'
                )
            ]) {
                sh '''
                    export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
                    export PATH=$JAVA_HOME/bin:$PATH

                    ./mvnw org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
  -Dsonar.projectKey=demo-app \
  -Dsonar.projectName=demo-app \
  -Dsonar.token=$SONAR_AUTH_TOKEN
                '''
            }
        }
    }
}


stage('Quality Gate') {
    steps {
        timeout(time: 5, unit: 'MINUTES') {
            waitForQualityGate abortPipeline: true
        }
    }
}


stage('Push Artifact to Nexus') {
    steps {
        withCredentials([
            usernamePassword(
                credentialsId: 'nexus-credentials',
                usernameVariable: 'NEXUS_USERNAME',
                passwordVariable: 'NEXUS_PASSWORD'
            )
        ]) {
            sh '''
                export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
                export PATH=$JAVA_HOME/bin:$PATH

                VERSION="0.0.1-feature-${BUILD_NUMBER}"

                ./mvnw versions:set \
                  -DnewVersion=$VERSION \
                  -DgenerateBackupPoms=false

                cat > settings.xml <<EOF
<settings>
  <servers>
    <server>
      <id>nexus-releases</id>
      <username>${NEXUS_USERNAME}</username>
      <password>${NEXUS_PASSWORD}</password>
    </server>
  </servers>
</settings>
EOF

                ./mvnw deploy \
                  -DskipTests \
                  -s settings.xml

                rm -f settings.xml
            '''
        }
    }
}


        stage('Deploy to EC2') {
            when {
                branch 'main'
            }
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'nexus-credentials',
                        usernameVariable: 'NEXUS_USERNAME',
                        passwordVariable: 'NEXUS_PASSWORD'
                    )
                ]) {
                    sh '''
                        export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
                        export PATH=$JAVA_HOME/bin:$PATH

                        VERSION="1.0.${BUILD_NUMBER}"

                        curl -u "$NEXUS_USERNAME:$NEXUS_PASSWORD" \
                          -o demo-app.jar \
                          "http://172.31.6.148:8081/repository/maven-releases/com/example/demo-app/${VERSION}/demo-app-${VERSION}.jar"

                        pkill -f 'demo-app.jar' || true

                        nohup java -jar demo-app.jar > app.log 2>&1 &

                        sleep 10

                        curl -f http://localhost:8080/health
                    '''
                }
            }
        }

    }
}
