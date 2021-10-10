pipeline {
    agent {
        dockerfile {
            filename 'Dockerfile'
            dir 'tools/dockers/android-sdk'
            args '-u root:root' //todo here because gradlew needs to access root folder /opt
        }
    }
    stages {
        stage("Assemble") {
            steps {
                sh "./gradlew clean assembleDebug"
            }
        }
        stage("Cleanup") {
            steps {
                sh "rm -R .gradle/ build app/build"
            }
        }
    }
}
