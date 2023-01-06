pipeline{
    environment {
        def imageName = "krashnat922/afourathon2.0:projectmanagement-be"
        mavenhome = tool name: 'Maven 3.5', type: 'maven'
        ENV_JOB_NAME = "projectmanagement-be"
        GIT_HELM_PATH = "helm"
        VERSION_TAG = "projectmanagement-be"
    }
    agent any
    stages{
        stage('Validate Helm chart') {
            steps {
                script {
                    CHART_FOLDER = "${WORKSPACE}/${GIT_HELM_PATH}"
                }
                sh "docker run -v ${WORKSPACE}:${WORKSPACE} alpine/helm lint ${CHART_FOLDER}"
            }
        }
        stage("Build") {
          steps{
            script {
                sh "${mavenhome}/bin/mvn clean package -DskipTests"            
            }
        }
    }
        stage("Build Docker Image") {
          steps{
            script {
              docker.withRegistry("", "ktapdiya-dockerhub") {
                  sh "docker build --network=host -t ${imageName} ."
                }
            }
        }
    }

        stage("Push Image to Registry") {
          steps{
            script {
              docker.withRegistry("", "ktapdiya-dockerhub") {
                  sh "docker push ${imageName}"
                  sh "docker rmi --force \$(docker images -q ${imageName} | uniq)"
                }
            }
        }
    }

        stage("Deploy service on MightyMinions Cluster") {
          steps {

            /*
            kubernetesDeploy(kubeconfigId: 'mightyminions-kubeconfig',
               configs: 'projectmanagement-be.yaml,projectmanagement-be-svc.yaml',
               enableConfigSubstitution: true)
            */

     echo "docker run -v ${WORKSPACE}:${WORKSPACE} dtzar/helm-kubectl:2.13.1 helm template --set image.tag=${VERSION_TAG} ${WORKSPACE}/${GIT_HELM_PATH} > ${WORKSPACE}/${ENV_JOB_NAME}-manifest.yaml" 
        sh "docker run -v ${WORKSPACE}:${WORKSPACE} dtzar/helm-kubectl:2.13.1 helm template --set image.tag=${VERSION_TAG} ${WORKSPACE}/${GIT_HELM_PATH} > ${WORKSPACE}/${ENV_JOB_NAME}-manifest.yaml"  

            withKubeConfig( credentialsId: 'mightyminions-kubeconfig' ) {
            sh '''
                kubectl apply -f ${ENV_JOB_NAME}-manifest.yaml
                #kubectl apply -f projectmanagement-be.yaml -f projectmanagement-be-svc.yaml
            '''
            
                }
            }
        }




    }
}