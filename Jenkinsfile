pipeline {
    agent {
        label 'common'
    }

    tools {
        jdk 'JDK_1_8'
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
                    sh "./gradlew build"
                }
            }
        }
    }
}
