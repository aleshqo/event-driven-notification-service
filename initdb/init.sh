#!/bin/bash
set -e

# Создание баз
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    CREATE DATABASE transaction_db;
    CREATE DATABASE account_db;
EOSQL

# Применяем схемы отдельно
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname="transaction_db" < /docker-entrypoint-initdb.d/2_transaction_schema.sql
psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname="account_db" < /docker-entrypoint-initdb.d/3_account_schema.sql
