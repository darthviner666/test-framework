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
    }

    tools {
        // Указываем инструменты из Global Tool Configuration
        maven 'MAVEN_3'  // Имя Maven из настроек Jenkins
        jdk 'JDK11'     // Имя JDK из настроек Jenkins (или jdk11)
    }

    stages {
        stage('Checkout') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: "*/${params.BRANCH_NAME}"]],
                    userRemoteConfigs: [[url: "${GIT_REPO}"]]
                ])
            }
        }

        stage('Environment Check') {
            steps {
                script {
                    echo "=== Проверка окружения ==="

                    sh """
                        echo "Java версия:"
                        java -version
                        echo ""

                        echo "Maven версия:"
                        mvn --version
                        echo ""

                        echo "Структура проекта:"
                        ls -la
                        echo ""

                        echo "XML файлы в проекте:"
                        find . -name "*.xml" -type f
                        echo ""

                        echo "Содержимое pom.xml (первые 30 строк):"
                        head -30 pom.xml
                    """
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    echo 'Сборка проекта...'

                    // Проверяем наличие pom.xml
                    if (fileExists('pom.xml')) {
                        echo "Найден pom.xml, запускаем сборку..."

                        // Показываем информацию о проекте
                        sh '''
                            echo "=== Информация о Maven проекте ==="
                            mvn help:effective-pom | grep -A5 -B5 "<groupId>\|<artifactId>\|<version>"
                            echo ""
                        '''

                        // Сначала компилируем
                        sh 'mvn clean compile -DskipTests'
                    } else {
                        error "pom.xml не найден! Проект не является Maven проектом."
                    }
                }
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    echo "Запуск TestNG suite: ${params.TEST_SUITE}"

                    // Поиск testng файлов
                    sh """
                        echo "Поиск файлов TestNG:"
                        find . -name "*.xml" -type f | grep -i test || echo "TestNG файлы не найдены"
                        echo ""

                        echo "Проверка директории test/resources:"
                        ls -la src/test/resources/ 2>/dev/null || echo "Директория не существует"
                        ls -la src/test/resources/suites/ 2>/dev/null || echo "Директория suites не существует"
                    """

                    // Определяем путь к testng файлу
                    def testngPath = ""

                    // Проверяем разные варианты расположения
                    sh '''
                        if [ -f "src/test/resources/suites/${params.TEST_SUITE}" ]; then
                            echo "Файл найден в src/test/resources/suites/"
                            echo "TESTNG_PATH=src/test/resources/suites/${params.TEST_SUITE}" > testng_path.txt
                        elif [ -f "src/test/resources/${params.TEST_SUITE}" ]; then
                            echo "Файл найден в src/test/resources/"
                            echo "TESTNG_PATH=src/test/resources/${params.TEST_SUITE}" > testng_path.txt
                        elif [ -f "${params.TEST_SUITE}" ]; then
                            echo "Файл найден в корне"
                            echo "TESTNG_PATH=${params.TEST_SUITE}" > testng_path.txt
                        else
                            echo "Файл не найден, используем все тесты"
                            echo "TESTNG_PATH=all" > testng_path.txt
                        fi
                    '''

                    // Читаем путь из файла
                    testngPath = readFile('testng_path.txt').trim().split('=')[1]

                    echo "Путь к TestNG файлу: ${testngPath}"
                        // Запускаем конкретный suite
                        sh "mvn test -Dsurefire.suiteXmlFiles=${testngPath}"
                }
            }
        }

        stage('Publish Results') {
            steps {
                script {
                    echo "Публикация результатов тестов..."

                    // Ищем отчеты
                    sh '''
                        echo "Поиск отчетов TestNG:"
                        find . -name "testng-results.xml" -type f 2>/dev/null | head -5
                        find . -name "surefire-reports" -type d 2>/dev/null | head -5
                        echo ""

                        echo "Содержимое target директории:"
                        ls -la target/ 2>/dev/null || echo "target директория не существует"
                    '''

                    // Публикация результатов TestNG
                    testng(
                        testResults: '**/testng-results.xml',
                        escapeTestDesription: false
                    )

                    // Публикация HTML отчетов
                    publishHTML([
                        reportDir: 'target/surefire-reports',
                        reportFiles: 'index.html',
                        reportName: 'TestNG HTML Report',
                        keepAll: true
                    ])
                }
            }
        }
    }

    post {
        always {
            echo "=== Завершение пайплайна ==="
            echo "Статус: ${currentBuild.currentResult}"
            echo "Номер сборки: ${env.BUILD_NUMBER}"

            // Архивация артефактов
            archiveArtifacts artifacts: 'target/*.jar, target/surefire-reports/**/*, logs/**/*',
                             allowEmptyArchive: true

            // Сохраняем дополнительные файлы
            sh '''
                echo "=== Файлы для отладки ==="
                ls -la
                echo ""
                echo "Логи тестов:"
                find . -name "*.log" -type f | head -10
            '''
        }

        success {
            echo '✅ Все этапы выполнены успешно!'
        }

        failure {
            echo '❌ Обнаружены ошибки в сборке!'

            // Сохраняем логи ошибок
            sh '''
                echo "=== Логи ошибок ==="
                find . -name "*.log" -type f -exec tail -50 {} \;
                echo ""
                echo "Последние ошибки Maven:"
                tail -100 maven.log 2>/dev/null || echo "Файл maven.log не найден"
            '''
        }
    }
}