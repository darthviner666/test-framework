Автотестовый фреймворк для UI и API тестов.
Команда для запуска: mvn clean test -Dsuite=suite_name
Команда для отчёта: allure serve
Используемые инструменты: Java 11, TestNG, RestAssured, Selenide, Selenoid, Log4j, Lombok, Owner, Allure, Faker, JsonPath, Maven, Cucumber
Примеры лог файлов находтся в папке logs (генерируются в target/logs)
Список задач:
Добавить восстановление кукис в веб драйвере,
Добавить отдельные xml для CI,
Настроить Selenoid на облаке,
Добавить видеозапись,
Добавить хранение тестовых данных с помощью JDBC, Hibernate
