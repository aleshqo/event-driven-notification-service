CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    request_id VARCHAR(64) NOT NULL UNIQUE,
    sender_id BIGINT NOT NULL,
    receiver_id BIGINT NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL,
    status VARCHAR(32) NOT NULL
);

CREATE INDEX idx_transactions_request_id ON transactions (request_id);
CREATE INDEX idx_transactions_status ON transactions (status);
