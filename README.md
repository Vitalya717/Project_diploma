# Дипломный проект по профессии «Тестировщик»
## Документы

1. **[Планирование автоматизации тестирования](https://github.com/Vitalya717/Project_diploma/blob/main/documents/Plan.md)**

2. **[Отчётные документы по итогам тестирования](https://github.com/Vitalya717/Project_diploma/blob/main/documents/Report.md)**

3. **[Отчётные документы по итогам автоматизации](https://github.com/Vitalya717/Project_diploma/blob/main/documents/Summary.md)**

## Замуск веб-приложения по адресу: http://localhost:8080/

**1. Для запуска контейнеров NodeJS, MySQL и PostgreSQL необходимо ввести в терминале команду:**
> * docker-compose up

**2. В новой вкладке терминала ввести команду в зависимости от БД**
   
   *MySQL:*
   > * java "-Dspring.datasource.url=jdbc:mysql://localhost:3306/app" -jar ./artifacts/aqa-shop.jar

   *PostgreSQL:*
   > * java "-Dspring.datasource.url=jdbc:postgresql://localhost:5432/app -jar" ./artifacts/aqa-shop.jar

## Запуск автотестов

**1. Для "MySQL", необходимо открыть новую вкладку терминала и ввести команду:**
> * ./gradlew test "-Dselenide.headless=true -Durlbd=jdbc:mysql://localhost:3306/app" --info

**2. Для "PostgreSQL", необходимо открыть новую вкладку терминала и ввести команду:**
> * ./gradlew test "-Dselenide.headless=true -Durlbd=jdbc:postgresql://localhost:5432/app" --info

## Отчеты автотестирования

**Для запуска и просмотра отчета с помощью "Allure", необходимо открыть новую вкладку терминала и ввести команду:**
> * ./gradlew allureServe

## Завершения работы SUT

**Для завершения работы, в терминале, где был запущен SUT, ввести команду:**
> * Ctrl+C

## Остановка и удаление контейнера

**Для остановки работы контейнеров "Docker-Compose", необходимо в терминал ввести следующую команду:**
> * docker-compose down




