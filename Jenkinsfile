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
                            //credentialsId: 'github-credentials'
                        ]]
                    ])
                }
            }
        }

        stage('Environment Check') {
                            steps {
                                script {
                                    echo "=== Проверка окружения ==="

                                    // Проверка Java
                                    bat """
                                        echo Java проверка:
                                        java -version 2>&1 || echo "Java не найдена"
                                        echo.
                                    """

                                    // Проверка Maven/Gradle
                                    bat """
                                        echo Maven проверка:
                                        mvn --version 2>&1 || echo "Maven не найден"
                                        echo.

                                    """

                                    // Проверка содержимого репозитория
                                    bat """
                                        echo Структура проекта:
                                        dir /s /b *.xml || echo "XML файлы не найдены"
                                        echo.

                                        echo Файлы в корне:
                                        dir
                                        echo.
                                    """
                                }
                            }
                        }

        // Этап 2: Очистка и установка зависимостей
stage('Build') {
            steps {
                script {
                    echo 'Сборка проекта...'

                    // Проверяем тип проекта
                    def buildFile = findFiles(glob: 'pom.xml')

                    if (!buildFile.isEmpty()) {
                        echo "Обнаружен Maven проект"
                        // Попробуем разные варианты сборки
                        try {
                            bat 'mvn clean compile -DskipTests'
                        } catch (Exception e1) {
                            echo "Стандартная сборка не удалась: ${e1.message}"
                            echo "Пробуем с обновлением зависимостей..."
                            try {
                                bat 'mvn clean compile -DskipTests -U'
                            } catch (Exception e2) {
                                echo "Сборка с обновлением не удалась: ${e2.message}"
                                echo "Пробуем только компиляцию..."
                                bat 'mvn compile -DskipTests'
                            }
                        }
                    } else {
                        echo "Файлы сборки не обнаружены. Проверяем структуру..."
                        bat 'dir /s'
                        error "Проект не содержит pom.xml или build.gradle"
                    }
                }
            }
        }



        // Этап 3: Запуск тестов TestNG
        stage('Run Tests') {
            steps {
                script {
                    echo "Запуск TestNG suite: ${params.TEST_SUITE}"

                    // Ищем файл testng.xml
                    def testngFiles = findFiles(glob: "**/${params.TEST_SUITE}")

                    if (testngFiles.isEmpty()) {
                        echo "Файл ${params.TEST_SUITE} не найден. Ищем все testng файлы..."
                        bat 'dir /s *.xml'
                        error "TestNG suite файл не найден"
                    } else {
                        echo "Найден файл: ${testngFiles[0].path}"

                        // Для Maven
                        bat "mvn clean test -Dsuite=${params.TEST_SUITE}"
                    }
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