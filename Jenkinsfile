pipeline {
  agent any
  stages {
    stage('Build Gradle') {
      steps {
        sh 'pwd'
        sh 'ls'
        sh './gradlew clean build --exclude-task test'
      }
    }

    stage('Build Docker') {
      steps {
        sh 'pwd'
      }
    }

  }
  environment {
    registryCredential = 'dockerhub_cred'
  }
}