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
        GIT_REPO = 'https://github.com/darthviner666/test-framework'
        WORKSPACE_DIR = "${env.WORKSPACE}"
        TEST_RESULTS_DIR = "${env.WORKSPACE}/test-output"
        HTML_REPORT_DIR = "${env.WORKSPACE}/target/surefire-reports"
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "Клонирование репозитория из ветки: ${params.BRANCH_NAME}"
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: "*/${params.BRANCH_NAME}"]],
                        userRemoteConfigs: [[
                            url: "${GIT_REPO}"
                            // credentialsId: 'github-credentials' если нужны
                        ]]
                    ])
                }
            }
        }

        stage('Environment Check') {
            steps {
                script {
                    echo "=== Проверка окружения ==="

                    // Проверка Java (используем sh для Linux)
                    sh """
                        echo "Java проверка:"
                        java -version 2>&1 || echo "Java не найдена"
                        echo ""
                    """

                    // Проверка Maven
                    sh """
                        echo "Maven проверка:"
                        mvn --version 2>&1 || echo "Maven не найден"
                        echo ""
                    """

                    // Проверка содержимого репозитория
                    sh """
                        echo "Структура проекта:"
                        find . -name "*.xml" -type f | head -20 || echo "XML файлы не найдены"
                        echo ""

                        echo "Файлы в корне:"
                        ls -la
                        echo ""
                    """
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    echo 'Сборка проекта...'

                    // Проверяем тип проекта
                    def buildFile = findFiles(glob: 'pom.xml')

                    if (!buildFile.isEmpty()) {
                        echo "Обнаружен Maven проект"

                        // Сначала показываем структуру pom.xml
                        sh """
                            echo "=== Информация о проекте ==="
                            echo "pom.xml найден в: ${buildFile[0].path}"
                            echo ""
                            echo "Первые 20 строк pom.xml:"
                            head -20 pom.xml
                            echo ""
                        """

                        // Попробуем разные варианты сборки
                        try {
                            sh 'mvn clean compile -DskipTests'
                        } catch (Exception e1) {
                            echo "Стандартная сборка не удалась: ${e1.message}"
                            echo "Пробуем с обновлением зависимостей..."
                            try {
                                sh 'mvn clean compile -DskipTests -U'
                            } catch (Exception e2) {
                                echo "Сборка с обновлением не удалась: ${e2.message}"
                                echo "Пробуем только компиляцию..."
                                sh 'mvn compile -DskipTests'
                            }
                        }
                    } else {
                        echo "Файлы сборки не обнаружены. Проверяем структуру..."
                        sh 'find . -type f -name "*.xml" -o -name "*.java" -o -name "pom.xml" -o -name "build.gradle" | head -30'
                        error "Проект не содержит pom.xml или build.gradle"
                    }
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    echo "Запуск TestNG suite: ${params.TEST_SUITE}"

                    // Ищем файл testng.xml
                    def testngFiles = findFiles(glob: "**/${params.TEST_SUITE}")

                    if (testngFiles.isEmpty()) {
                        echo "Файл ${params.TEST_SUITE} не найден. Ищем все testng файлы..."
                        sh 'find . -name "*.xml" -type f | head -20'

                        // Проверяем наличие testng.xml в стандартных местах
                        sh '''
                            echo "Проверяем стандартные расположения:"
                            ls -la src/test/resources/ 2>/dev/null || echo "src/test/resources/ не существует"
                            ls -la test-suites/ 2>/dev/null || echo "test-suites/ не существует"
                            find . -path "*/test/*" -name "*.xml" | head -10
                        '''
                        error "TestNG suite файл не найден"
                    } else {
                        echo "Найден файл: ${testngFiles[0].path}"

                        // Показываем содержимое testng файла
                        sh """
                            echo "=== Содержимое ${params.TEST_SUITE} ==="
                            cat "${testngFiles[0].path}" | head -30
                            echo ""
                        """

                        // Для Maven - правильные опции
                        sh "mvn clean test -Dsurefire.suiteXmlFiles=${testngFiles[0].path}"
                    }
                }
            }
        }

        stage('Publish Results') {
            steps {
                script {
                    echo "Публикация результатов..."

                    // Сначала находим отчеты
                    sh '''
                        echo "Поиск отчетов TestNG:"
                        find . -name "testng-results.xml" -type f 2>/dev/null
                        find . -name "surefire-reports" -type d 2>/dev/null
                        echo ""
                    '''

                    // Публикация результатов TestNG
                    testng(
                        testResults: '**/testng-results.xml',
                        escapeTestDesription: false,
                        escapeExceptionMessages: false
                    )

                    // Публикация HTML отчётов
                    publishHTML([
                        reportDir: '**/surefire-reports',
                        reportFiles: 'index.html, emailable-report.html',
                        reportName: 'TestNG HTML Report',
                        keepAll: true,
                        alwaysLinkToLastBuild: true
                    ])
                }
            }
        }
    }

    post {
        always {
            echo "Завершение пайплайна с статусом: ${currentBuild.currentResult}"

            // Архивация важных файлов
            archiveArtifacts artifacts: '**/target/*.jar, **/surefire-reports/**/*, **/test-output/**/*',
                             allowEmptyArchive: true

            // Сохраняем логи сборки
            sh '''
                echo "=== Сохранение логов ==="
                ls -la target/surefire-reports/ 2>/dev/null || echo "Нет surefire-reports"
                ls -la test-output/ 2>/dev/null || echo "Нет test-output"
            '''

            // Очистка workspace (опционально)
            // cleanWs()
        }
        success {
            echo '✅ Тесты успешно пройдены!'

            // Отправка email только если настроен почтовый сервер
            // emailext(
            //     to: 'denvviner.work@gmail.com',
            //     subject: "✅ Сборка ${env.JOB_NAME} - ${env.BUILD_NUMBER} успешна",
            //     body: "Успешная сборка: ${env.BUILD_URL}"
            // )
        }
        failure {
            echo '❌ Обнаружены падения в тестах!'

            // Сохраняем дополнительные логи при ошибке
            sh '''
                echo "=== Детали ошибки ==="
                find . -name "*.log" -type f | head -5
                echo "Последние ошибки Maven:"
                tail -100 maven.log 2>/dev/null || echo "Файл maven.log не найден"
            '''

            // Отправка email только если настроен почтовый сервер
            // emailext(
            //     to: 'denvviner.work@gmail.com',
            //     subject: "❌ Сборка ${env.JOB_NAME} - ${env.BUILD_NUMBER} упала",
            //     body: "Проверьте сборку: ${env.BUILD_URL}"
            // )
        }
    }
}