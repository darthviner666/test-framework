pipeline {
    agent any

    tools {
        jdk 'jdk11'
        maven 'Maven3'
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/darthviner666/test-framework',
                     branch: "${params.BRANCH_NAME}"
            }
        }

        stage('Environment Info') {
            steps {
                sh '''
                    echo "=== ИНФОРМАЦИЯ О СРЕДЕ ==="
                    echo "Java версия:"
                    java -version
                    echo ""
                    echo "Maven версия:"
                    mvn --version
                    echo ""
                    echo "Текущая директория:"
                    pwd
                    ls -la
                '''
            }
        }

        stage('Project Info') {
            steps {
                sh '''
                    echo "=== ИНФОРМАЦИЯ О ПРОЕКТЕ ==="
                    echo ""

                    echo "1. Проверка pom.xml:"
                    if [ -f "pom.xml" ]; then
                        echo "✅ pom.xml найден"
                        echo "Первые 20 строк:"
                        head -20 pom.xml
                    else
                        echo "❌ pom.xml не найден!"
                        exit 1
                    fi
                    echo ""

                    echo "2. Информация о Maven проекте:"
                    echo "Группа, артефакт, версия:"
                    mvn help:evaluate -Dexpression=project.groupId -q -DforceStdout 2>/dev/null || echo "Не удалось получить groupId"
                    mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout 2>/dev/null || echo "Не удалось получить artifactId"
                    mvn help:evaluate -Dexpression=project.version -q -DforceStdout 2>/dev/null || echo "Не удалось получить version"
                    echo ""

                    echo "3. Проверка TestNG файлов:"
                    echo "Доступные suite файлы:"
                    find . -name "*.xml" -path "*/suites/*" -type f 2>/dev/null | head -10 || echo "TestNG файлы не найдены"
                '''
            }
        }

        stage('Build') {
            steps {
                sh '''
                    echo "=== КОМПИЛЯЦИЯ ПРОЕКТА ==="
                    echo ""

                    echo "Запуск mvn clean compile..."
                    mvn clean compile -DskipTests

                    if [ $? -eq 0 ]; then
                        echo "✅ Компиляция успешна"
                    else
                        echo "❌ Ошибка компиляции"
                        echo "Пробуем альтернативный вариант..."
                        mvn compile -DskipTests
                    fi
                '''
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    echo "=== ЗАПУСК ТЕСТОВ ==="
                    echo "TestNG suite: ${params.TEST_SUITE}"

                    // Проверяем существование файла
                    def suitePath = "src/test/resources/suites/${params.TEST_SUITE}"

                    sh """
                        echo "Проверка файла: ${suitePath}"
                        if [ ! -f "${suitePath}" ]; then
                            echo "❌ Файл не найден!"
                            echo "Доступные suite файлы:"
                            find . -name "*.xml" -path "*/suites/*" -type f | head -10
                            exit 1
                        fi

                        echo "✅ Файл найден, запускаем тесты..."
                        echo "Команда: mvn test -Dsurefire.suiteXmlFiles=${suitePath}"

                        # Запускаем тесты
                        mvn test -Dsurefire.suiteXmlFiles="${suitePath}" -DskipTests=false
                    """
                }
            }
        }

        stage('Publish Results') {
            steps {
                script {
                    echo "Публикация результатов..."

                    // Проверяем наличие отчетов
                    sh '''
                        echo "Поиск отчетов..."
                        echo "TestNG результаты:"
                        find . -name "testng-results.xml" -type f 2>/dev/null | head -5
                        echo ""
                        echo "Директория отчетов:"
                        ls -la target/surefire-reports/ 2>/dev/null || echo "Директория отчетов не найдена"
                    '''

                    // Публикуем результаты TestNG
                    testng(
                        testResults: '**/testng-results.xml',
                        escapeTestDesription: false
                    )

                    // Публикуем HTML отчеты
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

    post {
        always {
            echo "=== СБОРКА ЗАВЕРШЕНА ==="
            echo "Статус: ${currentBuild.currentResult}"
            echo "Suite: ${params.TEST_SUITE}"

            // Сохраняем артефакты
            archiveArtifacts artifacts: 'target/surefire-reports/**/*',
                             allowEmptyArchive: true

        }

        success {
            echo '✅ Тесты успешно выполнены!'
        }

        failure {
            echo '❌ Обнаружены ошибки!'

            // Без проблемного синтаксиса find -exec
            sh '''
                echo "Логи ошибок:"
                echo "Список лог файлов:"
                LOG_FILES=$(find . -name "*.log" -type f | head -5)
                for log in $LOG_FILES; do
                    echo "=== $log ==="
                    tail -20 "$log"
                    echo ""
                done
            '''
        }
    }
}