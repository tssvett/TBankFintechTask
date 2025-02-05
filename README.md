# Translator Service
## Java Fintech Laboratory work
Проект представляет собой сервис для перевода слов на различные языки с использованием API Яндекса. Он разработан на языке Java и использует PostgreSQL для хранения данных, а также Docker для контейнеризации приложения.

## Используемые технологии

- Java
- PostgreSQL
- Liquibase
- Docker
- DBeaver
- Postman
- Swagger


## Установка и запуск проекта


1. **Клонируйте репозиторий**:

   Откройте командную строку и выполните команду:
   ```bash
   git clone https://github.com/tssvett/TBankFintechTask.git

2. **Перейдите в директорию проекта:**
    ```bash
    cd <путь до директории проекта>


3. **Cоздайте ваш API ключ яндекса:**

   [Создайте API ключ](https://yandex.cloud/ru/docs/translate/operations/sa-api-key?utm_referrer=https%3A%2F%2Fyandex.ru%2F)

3. **Добавьте ваш API ключ яндекса в файл .env:**

   Найдите файл .env и введите желаемые данные для бд и ваш API ключ.
   База данных создается автоматически по этим данным.
   ```
   YANDEX_API_KEY=<Ваш ключик для Yandex Translor API>
   DATABASE_NAME=<Придумайте название для бд>
   DATABASE_USERNAME=<придумайте имя пользователя для бд>
   DATABASE_PASSWORD=<придумайте пароль для бд>

4. **Запустите Docker:**

   Убедитесь, что у вас установлен Docker. Затем выполните команду для сборки и запуска контейнеров:
   ```bash
   docker-compose up -d

5. **Проверьте работу сервиса:**

   После успешного запуска сервиса вы можете импортировать коллекцию запросов t-bank-fintech-task.postman_collection.json в Postman для отправки запросов к API и проверки его работы.


6. **Просмотр API сервиса**

   Так же вы можете просмотреть API данного сервиса импортировав файл swaggerApi.yaml при помощи [Swagger](https://editor.swagger.io/)
## Примечания
Убедитесь, что Docker и Docker Compose установлены на вашем компьютере.
Проверьте, что переменные окружения в файле .env настроены правильно для доступа к вашей базе данных и API Яндекса.
