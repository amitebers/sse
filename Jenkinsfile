pipeline {

   agent any

   stages {

      stage('Build') {

         steps {

            git 'https://github.com/amitebers/jenkins.git'

            bat "mvn -Dmaven.test.failure.ignore=true clean package"

         }


         post {

            success {

               

               archiveArtifacts 'target/*.jar'

            }

         }

      }

   }

}