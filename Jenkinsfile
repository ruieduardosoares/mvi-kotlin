pipeline {
    agent {
        dockerfile {
            filename 'Dockerfile'
            dir 'tools/dockers/android-sdk'
            args '-u root:root' //todo here because gradlew needs to access root folder /opt
        }
    }
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage("Assemble") {
            steps {
                sh "./gradlew clean assembleDebug"
            }
        }
        stage("Tests") {
            steps {
                sh "./gradlew clean testDebugUnitTest"
            }
            post {
                success {
                    junit 'app/build/test-results/**/*.xml'
                }
            }
        }
    }
    post {
        //Here because we are overriding jenkins user with container root user
        //Always cleanup at the end regardless of pipeline status
        always {
            sh "rm -R .gradle/ build app/build"
        }
    }
}
