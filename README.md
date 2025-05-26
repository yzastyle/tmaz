# Тестовое приложение

Первым обязательным пунктом будет наличие Docker и Docker Compose.

## Шаги для развёртывания

1. **Клонировать репозиторий**
   ```bash
   git clone https://github.com/yzastyle/tmaz.git
   cd tmaz
2. **Переименовать файл конфигурации Spring Boot**
    ```bash
    mv application-ex.yml application-docker.yml 
3. **Настроить application-docker.yml**

Внутри файла заменить username и token на реальные
4. **Переименовать Docker Compose файл**
    ```bash
    mv docker-compose-ex.yml docker-compose.yml

5. **Указать ngrok токен**

В docker-compose.yml найдите переменную NGROK_AUTHTOKEN и подставьте ваш токен (https://dashboard.ngrok.com/get-started/setup/macos)
6. **Собрать проект локально**
    ```bash
    ./gradlew clean build -x test
7. **Запустить сборку контейнеров**
    ```bash
    docker-compose up --build
8. **Получить публичный URL Web UI**

В логах ngrok скопируйте адрес вида https://xxxxx.ngrok-free.app и укажите данный урл для своего бота через @BotFather
    
