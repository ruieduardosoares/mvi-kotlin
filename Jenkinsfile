pipeline {
    agent {
        dockerfile {
            filename 'Dockerfile'
            dir 'tools/dockers/android-sdk'
        }
    }
    stages {
        stage("Assemble") {
            steps {
                sh "./gradlew clean assembleDebug"
            }
        }
    }
}
