# Описание проекта

## Запуск проекта

- Первым делом нам надо собрать jar архивы с нашими spring boot приложениями. Для этого в терминале в корне нашего
  проект выполните команду:

**Для maven:** ```./mvnw clean package``` (если пишет Permission denied тогда сначала выполните chmod +x ./mvnw) или
```mvn clean package -Dskiptests```

```mvn clean package -Dmaven.test.skip```;

## Получение результатов

[Файл для отправки запросов на сервер](src/test/java/com/example/tickets/request.http)

`http://localhost:8080/ticket/average-time` - Получение среднего времени в минутах
`http://localhost:8080/ticket/percentile?value=90.00` - 90 можно заменить на иное для вычисления нового результата
процентиля. 

## Запуск проекта после формирования jar файла

`sh target/Tickets-0.0.1-SNAPSHOT.jar`