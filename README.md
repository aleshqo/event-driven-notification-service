[//]: # (TODO: https://github.com/aleshqo/event-driven-notification-service/issues/11 доработать README.md)

# Transaction Service with Kafka Notifications

## Overview

A microservice for processing financial transactions between users with event-driven architecture using Kafka for
notifications and providing transaction analytics.

## Features
- Process money transfers between accounts
- Kafka event notifications for successful transactions
- Transaction analytics (top users by transaction amount)
- REST API documentation with Swagger
- Dockerized deployment

### Prerequisites
- Docker 20.10+
- Docker Compose 1.29+
- JDK 21 (for local development)

### Running with Docker
```bash
git clone https://github.com/your-repo/transaction-service.git
docker-compose up -d
```
The service will be available at http://localhost:8080

### API Documentation
Swagger UI: http://localhost:8080/swagger-ui.html
