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


	stage('Check Java') {
    steps {
        sh '''
            echo "JAVA_HOME=$JAVA_HOME"
            java -version
            javac -version
            mvn -version
        '''
    }
	}

        stage('Build') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }

    }
}
