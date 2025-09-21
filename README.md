GigaLove Backend (Java / Spring Boot)

Запуск:
- Требуется Java 17+ и Maven
- В корне репозитория:

```
cd backend
mvn spring-boot:run
```

Сервис: http://localhost:8080

Эндпоинты (in-memory):
- POST /api/auth/register { email, phone, name, password } → { token, userId }
- POST /api/auth/login { email, password } → { token, userId }
- GET  /api/swipes
- POST /api/swipes/{userId}/like
- GET  /api/matches
- GET  /api/chat/{matchId}
- POST /api/chat/{matchId} { content }

CORS включён для localhost (Expo/Web).

