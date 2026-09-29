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
    }
}
