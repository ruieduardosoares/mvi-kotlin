def gradlew = "docker exec android-jenkins /app/gradlew -p /app "
pipeline {
    agent any
    stages {
        stage("Setup android docker") {
            steps {
                dir("tools/dockers/android-sdk") {
                    sh "./teardown.sh"
                    sh "./setup.sh"
                }
            }
        }
        stage("Assemble") {
            steps {
                sh "$gradlew clean assembleDebug"
            }
        }
        stage("Teardown android docker") {
            steps {
                dir("tools/dockers/android-sdk") {
                    sh "./teardown.sh"
                }
            }
        }
    }
}
