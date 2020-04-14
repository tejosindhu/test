pipeline {
   agent any
    tools {
      ant 'ant1.9.8'
    }
    
   stages {
      stage('Checkout') {
         steps {
            //cleanWs() 
            echo 'In Checkout Stage'
            checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/veersudhir83/devops-hackathon-test-suite.git']]])
         }
      }
      stage('Build') {
         steps {
            echo 'In Build stage'
            sh label: '', script: 'ant --version && cd ${WORKSPACE}/build/ && ant'
         }
      }      
   }
}
