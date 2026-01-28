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
                        docker --version

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

        stage('Build Project') {
            steps {
                sh '''
                    echo "=== СБОРКА ПРОЕКТА ==="
                    echo "Java:"
                    java -version
                    echo ""
                    echo "Maven:"
                    mvn --version
                    echo ""
                    echo "Компиляция..."
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
                        echo "Генерация Allure отчета..."
                        mvn allure:report -Pallure

                        echo "Архивация отчетов..."
                        ls -la target/site/allure-maven-plugin/
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
                        if [ -d "video" ]; then
                            echo "Найдены видео записи:"
                            ls -la video/
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

                // Очистка workspace
                cleanWs()
            }
        }

        success {
            echo '✅ Сборка успешно завершена!'
            // Можно добавить уведомления в Slack/Telegram/Email
        }

        failure {
            echo '❌ Сборка завершилась с ошибкой!'

            // Сохраняем логи при ошибке
            sh '''
                echo "=== ЛОГИ ОШИБОК ==="
                echo "Последние логи Docker:"
                docker logs $(docker ps -q -f name=selenoid) 2>/dev/null | tail -50 || echo "Docker логи не доступны"
                echo ""
                echo "Логи тестов:"
                find . -name "*.log" -type f | head -5 | xargs -I {} sh -c 'echo "=== {} ===" && tail -30 {}'
            '''
        }

        unstable {
            echo '⚠️ Сборка нестабильна (упавшие тесты)'
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

        mvn test -P${profiles} \\
            -Dsuite=${params.TEST_SUITE} \\
            -Dbrowser.name=${browser} \\
            -Denable.vnc=true \\
            -Denable.video=true \\
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

        mvn test -P${profiles} \\
            -Dsuite=${params.TEST_SUITE} \\
            -Dbrowser.name=${params.BROWSER} \\
            -Dselenide.headless=true
    """
}