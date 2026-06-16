# Урок 1: Flyway и «единый источник правды» для БД

## Проблема в твоём проекте

Было **три разных описания схемы**:

1. SQL в `initdb/` (старый: `balance DOUBLE`, без `reservations`)
2. JPA entity (`availableBalance`, `reservedBalance`, `Reservation`)
3. `ddl-auto: update` в account-service (Hibernate сам менял БД)

На собеседовании это называют **schema drift** — расхождение схемы между окружениями. Рекрутер не увидит, но при `docker compose up` сервис падает на `validate`.

## Решение: Flyway

**Flyway** применяет версионированные миграции `V1__....sql`, `V2__....sql` при старте приложения.

| Роль | Кто |
|------|-----|
| Источник правды | SQL-миграции в `src/main/resources/db/migration/` |
| JPA | Только маппинг (`ddl-auto: validate`) |
| initdb в Docker | Только `CREATE DATABASE` |

## Что запомнить

1. **Никогда** не полагайся на `ddl-auto: update` в prod и в портфолио.
2. Миграции **только вперёд** — не правь `V1` после merge, добавляй `V2`.
3. Seed-данные для демо — в отдельной миграции `V2__seed_demo_accounts.sql`.

## Проверь себя

```bash
docker compose down -v
docker compose up -d --build
# account:8082, transaction:8081
curl -X POST http://localhost:8081/api/transactions \
  -H 'Content-Type: application/json' \
  -d '{"senderId":1,"receiverId":2,"amount":10.00}'
```

Следующий урок (Фаза 1): integration test с Testcontainers повторит этот сценарий автоматически.
