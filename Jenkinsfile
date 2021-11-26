/**
 * MIT License
 *
 * Copyright (c) [2021] [Rui Eduardo Soares]
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

pipeline {
    agent {
        dockerfile {
            filename 'Dockerfile'
            dir 'tools/dockers/android-sdk'
            args '-u root:root' //here because gradlew needs to access root folder /opt
        }
    }
    options {
        skipStagesAfterUnstable()
    }
    stages {
        stage('Assemble') {
            steps {
                sh './gradlew assembleDebug'
            }
        }
        stage('Tests & Coverage') {
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

                    jacoco(
                            buildOverBuild            : false,
                            changeBuildStatus         : true,
                            minimumInstructionCoverage: '84',
                            minimumBranchCoverage     : '59',
                            maximumComplexityCoverage : '58',
                            minimumLineCoverage       : '94',
                            minimumMethodCoverage     : '74',
                            minimumClassCoverage      : '83',
                            execPattern               : '**/build/jacoco/*.exec',
                            classPattern              : '**/build/tmp/kotlin-classes',
                            sourcePattern             : 'src/main/kotlin',
                            exclusionPattern          : 'src/test/**'
                    )
                }
            }
        }
        stage('Code quality') {
            steps {
                sh './gradlew lintDebug'
                recordIssues tool: androidLintParser(pattern: 'app/build/reports/lint-results-debug.xml'), qualityGates: [[threshold: 3, type: 'TOTAL_HIGH', unstable: true], [threshold: 1, type: 'TOTAL_NORMAL', unstable: true], [threshold: 1, type: 'TOTAL_LOW', unstable: true]]

                sh './gradlew detektDebug'
                recordIssues tool: detekt(pattern: 'app/build/reports/detekt.xml'), qualityGates: [[threshold: 1, type: 'TOTAL_HIGH', unstable: true], [threshold: 1, type: 'TOTAL_NORMAL', unstable: true], [threshold: 1, type: 'TOTAL_LOW', unstable: true]]

                sh './gradlew cpdCheck'
                recordIssues tool: cpd(pattern: 'app/build/reports/cpd/cpdCheck.xml'), qualityGates: [[threshold: 1, type: 'TOTAL_HIGH', unstable: true], [threshold: 1, type: 'TOTAL_NORMAL', unstable: true], [threshold: 3, type: 'TOTAL_LOW', unstable: true]]

                recordIssues tool: taskScanner(includePattern: '*/src/**', highTags: 'FIXME', normalTags: 'TODO', lowTags: '@Deprecated', ignoreCase: true), qualityGates: [[threshold: 1, type: 'TOTAL_HIGH', unstable: true], [threshold: 1, type: 'TOTAL_NORMAL', unstable: true], [threshold: 1, type: 'TOTAL_LOW', unstable: true]]
            }
        }
        stage("Dexcount & Size") {
            steps {
                sh './gradlew :app:countDebugDexMethods'
                publishHTML(target: [allowMissing         : false,
                                     alwaysLinkToLastBuild: true,
                                     keepAll              : true,
                                     reportDir            : 'app/build/outputs/dexcount/debug/chart',
                                     reportFiles          : 'index.html',
                                     reportName           : 'Android Dex Count',
                                     reportTitles         : 'The Report'])

                plot csvFileName: 'plot-8e54e334-ab7b-4c9f-94f7-b9d8965723df.csv',
                        csvSeries: [[file: 'app/build/outputs/dexcount/debug/summary.csv', displayTableFlag: false, inclusionFlag: 'OFF']],
                        group: 'Android',
                        title: 'Dex Count',
                        style: 'line'

                sh './gradlew app:writeDebugAppAarSizeToCsv'
                plot csvFileName: 'plot-8e54e334-ab7b-4c9f-94f7-48484848.csv',
                        csvSeries: [[file: 'app/build/appsize.csv', displayTableFlag: false, inclusionFlag: 'OFF']],
                        group: 'Android',
                        title: 'AAR File size in MB',
                        style: 'line'
            }
        }
        stage("Generate Docs") {
            steps {
                sh './gradlew :app:dokkaJavadoc'
                publishHTML(target: [allowMissing         : false,
                                     alwaysLinkToLastBuild: true,
                                     keepAll              : true,
                                     reportDir            : 'app/build/dokka/javadoc',
                                     reportFiles          : 'index.html',
                                     reportName           : 'Kotlin Docs',
                                     reportTitles         : 'The Report'])
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
