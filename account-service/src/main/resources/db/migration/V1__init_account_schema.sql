CREATE TABLE accounts (
                          id                BIGSERIAL PRIMARY KEY,
                          number            VARCHAR(255)  NOT NULL UNIQUE,
                          owner_name        VARCHAR(255)  NOT NULL,
                          available_balance NUMERIC(19, 2) NOT NULL DEFAULT 0,
                          reserved_balance  NUMERIC(19, 2) NOT NULL DEFAULT 0,
                          version           BIGINT        NOT NULL DEFAULT 0
);

CREATE TABLE reservations (
                              request_id  VARCHAR(255) PRIMARY KEY,
                              account_id  BIGINT         NOT NULL,
                              amount      NUMERIC(19, 2) NOT NULL,
                              created_at  TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);

CREATE TABLE account_transactions (
                                      transaction_id  BIGINT PRIMARY KEY,
                                      account_id      BIGINT         NOT NULL,
                                      counterparty_id BIGINT         NOT NULL,
                                      amount          DOUBLE PRECISION NOT NULL,
                                      direction       VARCHAR(50)    NOT NULL,
                                      timestamp       TIMESTAMPTZ    NOT NULL,
                                      message         TEXT
);

CREATE INDEX idx_reservations_account_id ON reservations (account_id);
CREATE INDEX idx_account_transactions_account_id ON account_transactions (account_id);