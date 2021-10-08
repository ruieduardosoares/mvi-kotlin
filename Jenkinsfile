pipeline {
    agent any
    stages {
        stage('Setup android docker') {
            steps {
                dir('tools/dockers/android-sdk') {
                    sh 'setup.sh'
                }
            }
        }
        stage('Teardown android docker') {
            steps {
                dir('tools/dockers/android-sdk') {
                    sh 'teardown.sh'
                }
            }
        }
    }
}
