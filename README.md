# QA-java-diplom-2
В проекте тестируется программа, которая помогает заказать бургер в Stellar Burgers.
В данной части произведено покртытие ее Api-тестами.

## Использованные технологии и зависимости
Проект выполнен с использованием Java 11, Junit 4.13.2, rest-assured 5.3.1, gson 2.10.1, allure-junit4 и allure-rest-assured 2.24.0

## Список тестов
* CreateOrderTest
* CreateUserTest
* ListOfOrdersTest
* LoginUserTest
* UpdateUserTest

## Запуск тестов
Для запуска тестов необходимо запустить команду
```shell
mvn clean test
```

## Генерация отчета о тестировании
Для генерации отчета необходимо запустить команду в терминале находясь в папке проекта
```shell
allure serve target/surefire-reports/
```
Откроется окно браузера с отчётом.