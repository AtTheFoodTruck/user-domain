pipeline {
  agent any
  stages {
    stage('Build Gradle') {
      steps {
        sh '''pwd
ls'''
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