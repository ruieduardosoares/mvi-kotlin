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
        stage('Assemble') {
            steps {
                sh './gradlew clean assembleDebug'
            }
        }
        stage('Tests') {
            steps {
                sh './gradlew testDebugUnitTest'
            }
            post {
                success {
                    junit 'app/build/test-results/**/*.xml'

                    publishHTML(target: [allowMissing         : false,
                                         alwaysLinkToLastBuild: true,
                                         keepAll              : true,
                                         reportDir            : 'app/build/reports/tests/testDebugUnitTest',
                                         reportFiles          : 'index.html',
                                         reportName           : 'Tests',
                                         reportTitles         : 'The Report'])

                    step([$class                    : 'JacocoPublisher',
                          buildOverBuild            : true,
                          changeBuildStatus         : true,
                          minimumInstructionCoverage: '86',
                          minimumBranchCoverage     : '60',
                          minimumClassCoverage      : '88',
                          maximumComplexityCoverage : '61',
                          minimumLineCoverage       : '95',
                          minimumMethodCoverage     : '76',
                          execPattern               : '**/build/jacoco/*.exec',
                          classPattern              : '**/build/tmp/kotlin-classes',
                          sourcePattern             : 'src/main/java',
                          exclusionPattern          : 'src/test/**'])
                }
            }
        }
        stage('Code quality') {
            steps {

                sh './gradlew :app:countDebugDexMethods'
                publishHTML(target: [allowMissing         : false,
                                     alwaysLinkToLastBuild: true,
                                     keepAll              : true,
                                     reportDir            : 'app/build/outputs/dexcount/chart',
                                     reportFiles          : 'index.html',
                                     reportName           : 'Android Dex Count',
                                     reportTitles         : 'The Report'])

                plot csvFileName: 'plot-8e54e334-ab7b-4c9f-94f7-b9d8965723df.csv',
                        csvSeries: [[
                                            file            : 'summary.csv',
                                            exclusionValues : '',
                                            displayTableFlag: false,
                                            inclusionFlag   : 'OFF',
                                            url             : '']],
                        group: 'Plot Group',
                        title: 'Plot Title',
                        style: 'line',
                        exclZero: false,
                        keepRecords: false,
                        logarithmic: false,
                        numBuilds: '',
                        useDescr: false,
                        yaxis: '',
                        yaxisMaximum: '',
                        yaxisMinimum: ''

                sh './gradlew lintDebug'
                recordIssues tool: androidLintParser(pattern: 'app/build/reports/lint-results-debug.xml')

                sh './gradlew detektDebug'
                recordIssues tool: detekt(pattern: 'app/build/reports/detekt.xml')

                sh './gradlew cpdCheck'
                recordIssues tool: cpd(pattern: 'app/build/reports/cpd/cpdCheck.xml')
            }
        }
    }
    post {
        //Here because we are overriding jenkins user with container root user
        //Always cleanup at the end regardless of pipeline status
        always {
            sh 'rm -R .gradle/ build app/build'
        }
    }
}
