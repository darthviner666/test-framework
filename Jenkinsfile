pipeline {
    agent {
        docker {
            image 'maven:3.8.6-openjdk-11-slim'
            args '-v /root/.m2:/root/.m2 -v /var/jenkins_home/.m2:/var/jenkins_home/.m2'
            reuseNode true
        }
    }

    parameters {
        string(name: 'BRANCH_NAME',
               defaultValue: 'main',
               description: 'Имя ветки GitHub')
        string(name: 'TEST_SUITE',
               defaultValue: 'apiTests.xml',
               description: 'TestNG XML файл')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: "*/${params.BRANCH_NAME}"]],
                    userRemoteConfigs: [[
                        url: 'https://github.com/darthviner666/test-framework'
                    ]]
                ])
            }
        }

        stage('Build & Test') {
            steps {
                script {
                    echo "=== ИНФОРМАЦИЯ О СРЕДЕ ==="
                    sh '''
                        java -version
                        mvn --version
                        echo ""
                        echo "Доступные testng файлы:"
                        find . -name "*.xml" -path "*/suites/*" -type f
                    '''

                    def suitePath = "src/test/resources/suites/${params.TEST_SUITE}"
                    echo "Запуск suite: ${suitePath}"

                    sh """
                        mvn clean test -Dsurefire.suiteXmlFiles=${suitePath}
                    """
                }
            }
        }

        stage('Publish Results') {
            steps {
                testng(testResults: '**/testng-results.xml')
                publishHTML([
                    reportDir: 'target/surefire-reports',
                    reportFiles: 'index.html',
                    reportName: "Test Report - ${params.TEST_SUITE}",
                    keepAll: true
                ])
            }
        }
    }
}