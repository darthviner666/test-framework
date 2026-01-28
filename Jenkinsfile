pipeline {
    agent any

    tools {
        jdk 'jdk11'
        maven 'Maven3'
    }

    parameters {
        choice(
            name: 'BROWSER',
            choices: ['chrome', 'firefox', 'edge', 'all'],
            description: 'Браузер для тестирования'
        )
        choice(
            name: 'TEST_SUITE',
            choices: ['smokeTests.xml', 'apiTests.xml', 'uiTests.xml', 'dbTests.xml', 'cucumberTests.xml'],
            description: 'TestNG XML suite'
        )
        choice(
            name: 'RUN_MODE',
            choices: ['local', 'selenoid'],
            description: 'Режим запуска: local или selenoid'
        )
        booleanParam(
            name: 'PARALLEL',
            defaultValue: false,
            description: 'Запускать тесты параллельно'
        )
        booleanParam(
            name: 'GENERATE_ALLURE',
            defaultValue: true,
            description: 'Генерировать Allure отчет'
        )
    }

    environment {
        // Selenoid configuration
        SELENOID_URL = 'http://localhost:4444'
        // CI configuration
        CI = 'true'
        BUILD_TAG = "${env.JOB_NAME}-${env.BUILD_NUMBER}"
    }

    stages {
        stage('Prepare Environment') {
            steps {
                script {
                    echo "=== ПОДГОТОВКА ОКРУЖЕНИЯ ==="
                    echo "Job: ${env.JOB_NAME}"
                    echo "Build: ${env.BUILD_NUMBER}"
                    echo "Parameters:"
                    echo "  Browser: ${params.BROWSER}"
                    echo "  Test Suite: ${params.TEST_SUITE}"
                    echo "  Run Mode: ${params.RUN_MODE}"
                    echo "  Parallel: ${params.PARALLEL}"
                }
            }
        }

        stage('Start Selenoid') {
            when {
                expression { params.RUN_MODE == 'selenoid' }
            }
            steps {
                script {
                    echo "=== ЗАПУСК SELENOID ==="

                    sh '''
                        echo "Проверка Docker..."
                        docker --version || echo "Docker не установлен"

                        echo "Останавливаем старые контейнеры..."
                        docker-compose -f docker-compose-selenoid.yml down 2>/dev/null || true

                        echo "Запуск Selenoid..."
                        docker-compose -f docker-compose-selenoid.yml up -d

                        echo "Ожидание запуска Selenoid..."
                        sleep 10

                        echo "Проверка статуса Selenoid..."
                        curl -s ${SELENOID_URL}/status || echo "Selenoid не отвечает"
                    '''
                }
            }
        }

        stage('Checkout Code') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    userRemoteConfigs: [[
                        url: 'https://github.com/darthviner666/test-framework'
                    ]],
                    extensions: [
                        [$class: 'CleanBeforeCheckout'],
                        [$class: 'CloneOption', depth: 1, shallow: true]
                    ]
                ])
            }
        }

        stage('Project Information') {
            steps {
                sh '''
                    echo "=== ИНФОРМАЦИЯ О ПРОЕКТЕ ==="
                    echo ""

                    echo "1. Проверка pom.xml:"
                    if [ -f "pom.xml" ]; then
                        echo "✅ pom.xml найден"
                        echo "Основная информация:"
                        # Используем безопасный способ получить информацию о проекте
                        mvn help:evaluate -Dexpression=project.groupId -q -DforceStdout 2>/dev/null || echo "Не удалось получить groupId"
                        mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout 2>/dev/null || echo "Не удалось получить artifactId"
                        mvn help:evaluate -Dexpression=project.version -q -DforceStdout 2>/dev/null || echo "Не удалось получить version"
                    else
                        echo "❌ pom.xml не найден!"
                        exit 1
                    fi
                    echo ""

                    echo "2. Проверка TestNG файлов:"
                    echo "Доступные suite файлы:"
                    find . -name "*.xml" -path "*/suites/*" -type f 2>/dev/null | head -10 || echo "TestNG файлы не найдены"
                '''
            }
        }

        stage('Build Project') {
            steps {
                sh '''
                    echo "=== СБОРКА ПРОЕКТА ==="
                    echo "Java версия:"
                    java -version
                    echo ""
                    echo "Maven версия:"
                    mvn --version
                    echo ""
                    echo "Компиляция проекта..."
                    mvn clean compile -DskipTests
                '''
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    echo "=== ЗАПУСК ТЕСТОВ ==="

                    // Определяем активные профили
                    def activeProfiles = ['ci']

                    if (params.RUN_MODE == 'selenoid') {
                        if (params.BROWSER == 'all') {
                            // Запускаем все браузеры последовательно
                            def browsers = ['chrome', 'firefox', 'edge']
                            for (browser in browsers) {
                                runTestsForBrowser(browser)
                            }
                        } else {
                            // Запускаем один браузер
                            runTestsForBrowser(params.BROWSER)
                        }
                    } else {
                        // Локальный запуск
                        runLocalTests()
                    }
                }
            }
        }

        stage('Generate Allure Report') {
            when {
                expression { params.GENERATE_ALLURE }
            }
            steps {
                script {
                    echo "=== ГЕНЕРАЦИЯ ALLURE ОТЧЕТА ==="

                    sh '''
                        echo "Сборка Allure результатов..."
                        if [ -d "target/allure-results" ]; then
                            echo "Allure результаты найдены"
                            ls -la target/allure-results/
                        else
                            echo "Allure результаты не найдены, собираем..."
                            mvn allure:report -Pallure 2>/dev/null || echo "Не удалось сгенерировать Allure отчет"
                        fi
                    '''

                    // Публикация Allure отчета
                    allure([
                        includeProperties: false,
                        jdk: '',
                        properties: [],
                        reportBuildPolicy: 'ALWAYS',
                        results: [[path: 'target/allure-results']]
                    ])

                    // Публикация HTML отчета
                    publishHTML([
                        reportDir: 'target/site/allure-maven-plugin',
                        reportFiles: 'index.html',
                        reportName: 'Allure Report',
                        keepAll: true
                    ])
                }
            }
        }

        stage('Archive Artifacts') {
            steps {
                script {
                    echo "=== АРХИВАЦИЯ АРТЕФАКТОВ ==="

                    // Архивация видео (если есть)
                    sh '''
                        echo "Поиск артефактов..."
                        if [ -d "video" ]; then
                            echo "Найдены видео записи:"
                            ls -la video/
                        fi

                        if [ -d "target/surefire-reports" ]; then
                            echo "Найдены отчеты surefire:"
                            ls -la target/surefire-reports/
                        fi

                        if [ -d "logs" ]; then
                            echo "Найдены логи:"
                            ls -la logs/
                        fi
                    '''

                    archiveArtifacts artifacts: 'target/**/*, logs/**/*, video/**/*, **/surefire-reports/**/*',
                                     allowEmptyArchive: true

                    // Публикация результатов TestNG
                    testng(testResults: '**/testng-results.xml')
                }
            }
        }
    }

    post {
        always {
            script {
                echo "=== ЗАВЕРШЕНИЕ СБОРКИ ==="
                echo "Статус: ${currentBuild.currentResult}"
                echo "Длительность: ${currentBuild.durationString}"

                // Останавливаем Selenoid если запускали
                if (params.RUN_MODE == 'selenoid') {
                    sh '''
                        echo "Остановка Selenoid..."
                        docker-compose -f docker-compose-selenoid.yml down 2>/dev/null || true
                    '''
                }

                // Сохраняем информацию о сборке
                sh '''
                    echo "Информация о сборке:"
                    echo "Версия Java:"
                    java -version 2>&1 | head -1
                    echo ""
                    echo "Версия Maven:"
                    mvn --version 2>&1 | head -1
                '''
            }
        }

        success {
            echo '✅ Сборка успешно завершена!'

            // Можно добавить уведомления
            sh '''
                echo "Все тесты пройдены успешно!"
                echo "Allure отчет доступен по ссылке выше"
            '''
        }

        failure {
            echo '❌ Сборка завершилась с ошибкой!'

            // Сохраняем логи при ошибке
            sh '''
                echo "=== ЛОГИ ОШИБОК ==="
                echo "Поиск лог файлов..."
                LOG_FILES=$(find . -name "*.log" -type f | head -5)
                if [ -n "$LOG_FILES" ]; then
                    for log in $LOG_FILES; do
                        echo "=== $log (последние 20 строк) ==="
                        tail -20 "$log"
                        echo ""
                    done
                else
                    echo "Лог файлы не найдены"
                fi

                echo "Проверка Docker контейнеров:"
                docker ps -a 2>/dev/null || echo "Docker не доступен"
            '''
        }

        unstable {
            echo '⚠️ Сборка нестабильна (упавшие тесты)'

            sh '''
                echo "Некоторые тесты упали. Проверьте отчеты выше."
            '''
        }
    }
}

// Метод для запуска тестов для конкретного браузера
def runTestsForBrowser(browser) {
    echo "Запуск тестов для браузера: ${browser}"

    def profiles = "ci,selenoid-${browser}"
    if (params.PARALLEL) {
        profiles += ",parallel"
    }

    sh """
        echo "Активные профили: ${profiles}"
        echo "Запуск suite: ${params.TEST_SUITE}"
        echo "Браузер: ${browser}"
        echo "Режим: remote (Selenoid)"
        echo ""

        mvn test -P${profiles} \
            -Dsuite=${params.TEST_SUITE} \
            -Dbrowser.name=${browser} \
            -Denable.vnc=true \
            -Denable.video=true \
            -Dselenide.remote=${env.SELENOID_URL}/wd/hub
    """
}

// Метод для локального запуска тестов
def runLocalTests() {
    echo "Локальный запуск тестов"

    def profiles = "ci"
    if (params.BROWSER != 'all') {
        profiles += ",local-${params.BROWSER}"
    }
    if (params.PARALLEL) {
        profiles += ",parallel"
    }

    sh """
        echo "Активные профили: ${profiles}"
        echo "Запуск suite: ${params.TEST_SUITE}"
        echo "Браузер: ${params.BROWSER}"
        echo "Режим: local"
        echo ""

        mvn test -P${profiles} \
            -Dsuite=${params.TEST_SUITE} \
            -Dbrowser.name=${params.BROWSER} \
            -Dselenide.headless=true
    """
}