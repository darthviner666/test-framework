pipeline {
    agent any

    parameters {
        string(name: 'BRANCH_NAME',
               defaultValue: 'main',
               description: 'Имя ветки GitHub для тестирования')
        string(name: 'TEST_SUITE',
               defaultValue: 'testng.xml',
               description: 'Название XML файла с suite TestNG')
    }

    environment {
        // Настройка переменных окружения
        GIT_REPO = 'https://github.com/darthviner666/test-framework'
        WORKSPACE_DIR = "${env.WORKSPACE}"
        TEST_RESULTS_DIR = "${env.WORKSPACE}/test-output"
        HTML_REPORT_DIR = "${env.WORKSPACE}/target/surefire-reports"
    }

    stages {
        // Этап 1: Получение кода из репозитория
        stage('Checkout') {
            steps {
                script {
                    echo "Клонирование репозитория из ветки: ${params.BRANCH_NAME}"
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: "*/${params.BRANCH_NAME}"]],
                        userRemoteConfigs: [[
                            url: "${GIT_REPO}",
                            credentialsId: 'github-credentials'
                        ]]
                    ])
                }
            }
        }

        // Этап 2: Очистка и установка зависимостей
        stage('Build') {
            steps {
                echo 'Сборка проекта...'
                // Для Maven
                bat 'mvn clean compile -DskipTests'
                // Или для Gradle
                // bat 'gradle clean compileJava'
            }
        }

        // Этап 3: Запуск тестов TestNG
        stage('Run Tests') {
            steps {
                script {
                    echo "Запуск TestNG suite: ${params.TEST_SUITE}"
                    // Для Maven
                    bat "mvn test -Dtestng.suite.xml=${params.TEST_SUITE}"
                    // Или прямой вызов TestNG
                    // bat "java -cp \"target/classes;target/test-classes;lib/*\" org.testng.TestNG ${params.TEST_SUITE}"
                }
            }
        }

        // Этап 4: Публикация результатов
        stage('Publish Results') {
            steps {
                // Публикация результатов TestNG
                testng(
                    testResults: 'test-output/testng-results.xml',
                    escapeTestDesription: false,
                    escapeExceptionMessages: false
                )

                // Публикация HTML отчётов (если есть)
                publishHTML([
                    reportDir: "${HTML_REPORT_DIR}",
                    reportFiles: 'emailable-report.html',
                    reportName: 'TestNG HTML Report',
                    keepAll: true
                ])
            }
        }
    }

    post {
        always {
            echo "Завершение пайплайна с статусом: ${currentBuild.currentResult}"
                        emailext(
                            to: 'denvviner.work@gmail.com',
                            subject: "Сборка ${env.JOB_NAME} - ${env.BUILD_NUMBER} успешна",
                            body: "Успешная сборка: ${env.BUILD_URL}"
                        )
            // Очистка workspace при необходимости
            cleanWs()
        }
        success {
            echo 'Тесты успешно пройдены!'
        }
        failure {
            echo 'Обнаружены падения в тестах!'
            // Отправка уведомления
            emailext(
                to: 'denvviner.work@gmail.com',
                subject: "Сборка ${env.JOB_NAME} - ${env.BUILD_NUMBER} упала",
                body: "Проверьте сборку: ${env.BUILD_URL}"
            )
        }
    }
}