-- Внимание: будет применён к account_db

CREATE TABLE accounts (
                          id BIGSERIAL PRIMARY KEY,
                          number VARCHAR(255) NOT NULL,
                          owner_name VARCHAR(255) NOT NULL,
                          balance DOUBLE PRECISION NOT NULL
);

CREATE TABLE account_transactions (
                                      transaction_id BIGINT PRIMARY KEY,
                                      account_id BIGINT NOT NULL,
                                      counterparty_id BIGINT NOT NULL,
                                      amount DOUBLE PRECISION NOT NULL,
                                      direction VARCHAR(50) NOT NULL,
                                      timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
                                      message TEXT
);

ALTER TABLE accounts ADD CONSTRAINT uk_account_number UNIQUE (number);

CREATE INDEX idx_account_transactions_account_id ON account_transactions(account_id);
CREATE INDEX idx_account_transactions_counterparty_id ON account_transactions(counterparty_id);
CREATE INDEX idx_account_transactions_timestamp ON account_transactions(timestamp);
