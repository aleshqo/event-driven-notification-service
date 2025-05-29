-- Внимание: этот файл будет запускаться скриптом init.sh и подключаться к transaction_db

CREATE TABLE processed_requests (
                                    request_id VARCHAR(255) PRIMARY KEY,
                                    processed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE transactions (
                              id BIGSERIAL PRIMARY KEY,
                              sender_id BIGINT NOT NULL,
                              receiver_id BIGINT NOT NULL,
                              amount DOUBLE PRECISION NOT NULL,
                              timestamp TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_transactions_sender_id ON transactions(sender_id);
CREATE INDEX idx_transactions_receiver_id ON transactions(receiver_id);
CREATE INDEX idx_transactions_timestamp ON transactions(timestamp);
