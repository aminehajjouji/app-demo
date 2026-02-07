pipeline {
  agent any
  environment {
    IMAGE_NAME = 'mahajjouji/app-demo'
    IMAGE_TAG = 'latest'
  }
  stages {
    stage('Precheck Tools') {
      steps {
        sh 'mvn -v || true'
        sh 'docker version || true'
        sh 'docker compose version || true'
      }
    }
    stage('Checkout') {
      steps {
        echo 'SCM checkout is managed by job configuration (Pipeline script mode)'
      }
    }
    stage('Build') {
      steps {
        sh 'mvn -B clean package -DskipTests'
      }
    }
    stage('Test') {
      steps {
        sh 'mvn test'
      }
    }
    stage('Docker Build') {
      steps {
        sh 'docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .'
      }
    }
    stage('Docker Login & Push') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
          sh 'echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin'
          sh 'docker push ${IMAGE_NAME}:${IMAGE_TAG}'
        }
      }
    }
    stage('Deploy') {
      steps {
        sh 'docker compose up -d --build'
      }
    }
  }
}
