# Фаза 0 — пошаговая реализация (скопируй или попроси Agent применить)

> **Статус:** дубликаты `com/example/**` уже удалены из репозитория.

## Шаг 1. Flyway в Gradle

### `gradle/libs.versions.toml` — замени kotlin на stable

```toml
kotlin = "1.9.25"
```

### `account-service/build.gradle` — добавь

```gradle
implementation libs.flyway.core
```

### `transaction-service/build.gradle` — добавь

```gradle
implementation libs.flyway.core
```

---

## Шаг 2. Миграции account-service

Файл: `account-service/src/main/resources/db/migration/V1__init_account_schema.sql`

```sql
CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    number VARCHAR(255) NOT NULL UNIQUE,
    owner_name VARCHAR(255) NOT NULL,
    available_balance NUMERIC(19, 2) NOT NULL DEFAULT 0,
    reserved_balance NUMERIC(19, 2) NOT NULL DEFAULT 0,
    version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE reservations (
    request_id VARCHAR(255) PRIMARY KEY,
    account_id BIGINT NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE account_transactions (
    transaction_id BIGINT PRIMARY KEY,
    account_id BIGINT NOT NULL,
    counterparty_id BIGINT NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    direction VARCHAR(50) NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL,
    message TEXT
);
```

Файл: `account-service/src/main/resources/db/migration/V2__seed_demo_accounts.sql`

```sql
INSERT INTO accounts (number, owner_name, available_balance, reserved_balance, version)
VALUES ('ACC-001', 'Alice', 1000.00, 0, 0),
       ('ACC-002', 'Bob', 500.00, 0, 0);
```

### `account-service/src/main/resources/application.yml`

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
  flyway:
    enabled: true
    locations: classpath:db/migration
```

---

## Шаг 3. Миграции transaction-service

Файл: `transaction-service/src/main/resources/db/migration/V1__init_transaction_schema.sql`

```sql
CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    request_id VARCHAR(255) NOT NULL UNIQUE,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    status VARCHAR(50) NOT NULL
);

CREATE INDEX idx_transactions_request_id ON transactions (request_id);
```

### `transaction-service/src/main/resources/application.yml`

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
  flyway:
    enabled: true
```

Удали `ddl-auto: update` из `application-dev.yml`.

---

## Шаг 4. Docker initdb — только базы

### `initdb/init.sh`

```bash
#!/bin/bash
set -e
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE transaction_db;
    CREATE DATABASE account_db;
EOSQL
```

Убери монтирование `2_transaction_schema.sql` и `3_account_schema.sql` из `docker-compose.yml` (volumes postgres).

---

## Шаг 5. RestClient → account-service в Docker

### `transaction-service/.../RestClientConfig.kt`

```kotlin
@Configuration
class RestClientConfig(
    @Value("\${app.account-service.base-url:http://localhost:8082}") private val baseUrl: String,
) {
    @Bean
    fun restClient(): RestClient =
        RestClient.builder().baseUrl(baseUrl).build()
}
```

### `docker-compose.yml` (transaction-service environment)

```yaml
APP_ACCOUNT_SERVICE_BASE_URL: http://account-service:8080
```

---

## Шаг 6. Notification (минимум)

### `common-lib/.../event/TransferCompletedEvent.kt`

```kotlin
data class TransferCompletedEvent(
    val requestId: String,
    val senderId: Long,
    val receiverId: Long,
    val amount: BigDecimal,
    val completedAt: Instant,
)
```

После `CONFIRMED` в `TransactionProcessingService` — publish в topic `transfer-completed`.

### `notification-service` — KafkaListener на `transfer-completed`, лог «Email sent to...».

Убери `datasource` из `application.yaml` notification (пока БД не нужна).

---

## Шаг 7. Проверка

```bash
docker compose down -v && docker compose up -d --build
curl -X POST http://localhost:8081/api/transactions \
  -H 'Content-Type: application/json' \
  -d '{"senderId":1,"receiverId":2,"amount":10.00}'
docker compose logs notification-service | grep -i email
```

---

## Что изучили

| Тема | Зачем |
|------|-------|
| Flyway | Воспроизводимая схема для команды и CI |
| validate | JPA не «тихо» ломает prod |
| Seed V2 | Демо без ручного SQL |
| transfer-completed | Отдельное событие для read-side (notification) |

**Следующий урок (Фаза 1):** первый integration test на Testcontainers.
