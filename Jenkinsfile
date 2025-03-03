pipeline {
    agent any

    environment {
        MAVEN_HOME = tool 'Maven'
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/AEIS-LLC/automation-cc.git'
            }
        }

        stage('Setup Maven') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Run Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Publish TestNG Report') {
            steps {
                publishTestNGResult testResultsPattern: '**/test-output/testng-results.xml'
            }
        }

        stage('Email Notification') {
            steps {
                emailext subject: "Test Execution Report",
                         body: "Test execution completed. Check Jenkins for details.",
                         recipientProviders: [developers()]
            }
        }
    }

   post {
        always {
            archiveArtifacts artifacts: '**/test-output/*.html', fingerprint: true
        }
        failure {
            mail to: 'akumar@ndtatlas.com',
                 subject: "Build Failed: ${env.JOB_NAME}",
                 body: "Check Jenkins for details."
        }
    }
}
