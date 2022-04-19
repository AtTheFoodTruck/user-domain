pipeline {
  agent any
  stages {
    stage('Build Gradle') {
      steps {
        sh 'pwd'
        sh 'ls'
        sh '''sudo chmod 777 gradlew
./gradlew clean build --exclude-task test'''
      }
    }

    stage('Build Docker') {
      steps {
        script {
          backend_user = docker.build("goalgoru/backend_user")
        }

      }
    }

  }
  environment {
    registryCredential = 'dockerhub_cred'
  }
}