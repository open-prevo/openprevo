pipeline {
    agent {
        label 'common'
    }

    parameters {
        string(defaultValue: "", description: 'Additional gradle arguments', name: 'GRADLE_ARGS')
    }

    options {
        skipStagesAfterUnstable()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '28'))
        timeout(time: 1, unit: 'HOURS')
    }

    triggers {
        // at least once a day
        cron('H 12 * * *')
        // every thirty minutes
        pollSCM('H/30 * * * *')
    }

    stages {
        stage("Git Checkout") {
            steps {
                deleteDir()
                checkout scm
            }
        }

        stage("Gradle Build (compile and test)") {
            steps {
                script {
                    sh "./gradlew build ${params.GRADLE_ARGS}"
                }
            }
        }
    }
}
