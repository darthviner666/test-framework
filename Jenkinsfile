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
        GIT_REPO = 'https://github.com/YOUR-USERNAME/YOUR-REPO.git'
        CREDENTIALS_ID = 'your-credentials-id'
        JAVA_HOME = 'C:\\Program Files\\Java\\jdk-11' // Для Windows
        // Для Linux: JAVA_HOME = '/usr/lib/jvm/java-11-openjdk'
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
                            url: "${GIT_REPO}",
                            credentialsId: "${CREDENTIALS_ID}"
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

                        echo Gradle проверка:
                        gradle --version 2>&1 || echo "Gradle не найден"
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

        stage('Build') {
            steps {
                script {
                    echo 'Сборка проекта...'

                    // Проверяем тип проекта
                    def buildFile = findFiles(glob: 'pom.xml')
                    def gradleFile = findFiles(glob: 'build.gradle')

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
                    } else if (!gradleFile.isEmpty()) {
                        echo "Обнаружен Gradle проект"
                        bat 'gradle clean compileJava'
                    } else {
                        echo "Файлы сборки не обнаружены. Проверяем структуру..."
                        bat 'dir /s'
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
                        bat 'dir /s *.xml'
                        error "TestNG suite файл не найден"
                    } else {
                        echo "Найден файл: ${testngFiles[0].path}"

                        // Для Maven
                        bat "mvn test -Dtestng.suite.xml=${params.TEST_SUITE}"
                    }
                }
            }
        }

        stage('Publish Results') {
            steps {
                script {
                    echo "Публикация результатов..."

                    // Публикация результатов TestNG
                    testng(
                        testResults: '**/testng-results.xml',
                        escapeTestDesription: false
                    )

                    // Поиск и публикация HTML отчетов
                    publishHTML([
                        reportDir: '**/surefire-reports',
                        reportFiles: 'index.html, emailable-report.html',
                        reportName: 'TestNG HTML Report',
                        keepAll: true
                    ])
                }
            }
        }
    }

    post {
        always {
            echo "=== Сборка завершена ==="
            echo "Статус: ${currentBuild.currentResult}"
            echo "Номер сборки: ${env.BUILD_NUMBER}"
            echo "URL сборки: ${env.BUILD_URL}"

            // Архивация артефактов
            archiveArtifacts artifacts: '**/target/*.jar, **/build/libs/*.jar', allowEmptyArchive: true
            archiveArtifacts artifacts: '**/test-output/**/*', allowEmptyArchive: true
            archiveArtifacts artifacts: '**/target/surefire-reports/**/*', allowEmptyArchive: true
        }
        success {
            echo '✅ Все этапы выполнены успешно!'
        }
        failure {
            echo '❌ Обнаружены ошибки в сборке'

            // Отправка уведомления
            emailext(
                to: 'team@example.com',
                subject: "Сборка ${env.JOB_NAME} #${env.BUILD_NUMBER} упала",
                body: """
                    Произошла ошибка в сборке.

                    Детали:
                    - Задание: ${env.JOB_NAME}
                    - Номер сборки: ${env.BUILD_NUMBER}
                    - Ссылка: ${env.BUILD_URL}
                    - Причина: Проверьте логи сборки

                    Необходимо проверить:
                    1. Наличие pom.xml/build.gradle
                    2. Корректность testng.xml
                    3. Доступность зависимостей Maven
                """
            )
        }
        unstable {
            echo '⚠️ Сборка нестабильна (упавшие тесты)'
        }
    }
}pipeline {
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
         GIT_REPO = 'https://github.com/YOUR-USERNAME/YOUR-REPO.git'
         CREDENTIALS_ID = 'your-credentials-id'
         JAVA_HOME = 'C:\\Program Files\\Java\\jdk-11' // Для Windows
         // Для Linux: JAVA_HOME = '/usr/lib/jvm/java-11-openjdk'
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
                             url: "${GIT_REPO}",
                             credentialsId: "${CREDENTIALS_ID}"
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

                         echo Gradle проверка:
                         gradle --version 2>&1 || echo "Gradle не найден"
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

         stage('Build') {
             steps {
                 script {
                     echo 'Сборка проекта...'

                     // Проверяем тип проекта
                     def buildFile = findFiles(glob: 'pom.xml')
                     def gradleFile = findFiles(glob: 'build.gradle')

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
                     } else if (!gradleFile.isEmpty()) {
                         echo "Обнаружен Gradle проект"
                         bat 'gradle clean compileJava'
                     } else {
                         echo "Файлы сборки не обнаружены. Проверяем структуру..."
                         bat 'dir /s'
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
                         bat 'dir /s *.xml'
                         error "TestNG suite файл не найден"
                     } else {
                         echo "Найден файл: ${testngFiles[0].path}"

                         // Для Maven
                         bat "mvn test -Dtestng.suite.xml=${params.TEST_SUITE}"
                     }
                 }
             }
         }

         stage('Publish Results') {
             steps {
                 script {
                     echo "Публикация результатов..."

                     // Публикация результатов TestNG
                     testng(
                         testResults: '**/testng-results.xml',
                         escapeTestDesription: false
                     )

                     // Поиск и публикация HTML отчетов
                     publishHTML([
                         reportDir: '**/surefire-reports',
                         reportFiles: 'index.html, emailable-report.html',
                         reportName: 'TestNG HTML Report',
                         keepAll: true
                     ])
                 }
             }
         }
     }

     post {
         always {
             echo "=== Сборка завершена ==="
             echo "Статус: ${currentBuild.currentResult}"
             echo "Номер сборки: ${env.BUILD_NUMBER}"
             echo "URL сборки: ${env.BUILD_URL}"

             // Архивация артефактов
             archiveArtifacts artifacts: '**/target/*.jar, **/build/libs/*.jar', allowEmptyArchive: true
             archiveArtifacts artifacts: '**/test-output/**/*', allowEmptyArchive: true
             archiveArtifacts artifacts: '**/target/surefire-reports/**/*', allowEmptyArchive: true
         }
         success {
             echo '✅ Все этапы выполнены успешно!'
         }
         failure {
             echo '❌ Обнаружены ошибки в сборке'

             // Отправка уведомления
             emailext(
                 to: 'team@example.com',
                 subject: "Сборка ${env.JOB_NAME} #${env.BUILD_NUMBER} упала",
                 body: """
                     Произошла ошибка в сборке.

                     Детали:
                     - Задание: ${env.JOB_NAME}
                     - Номер сборки: ${env.BUILD_NUMBER}
                     - Ссылка: ${env.BUILD_URL}
                     - Причина: Проверьте логи сборки

                     Необходимо проверить:
                     1. Наличие pom.xml/build.gradle
                     2. Корректность testng.xml
                     3. Доступность зависимостей Maven
                 """
             )
         }
         unstable {
             echo '⚠️ Сборка нестабильна (упавшие тесты)'
         }
     }
 }