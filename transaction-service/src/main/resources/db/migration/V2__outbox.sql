CREATE TABLE outbox_events (
    id          BIGSERIAL PRIMARY KEY,
    topic       VARCHAR(128)  NOT NULL,
    message_key VARCHAR(64)   NOT NULL,
    payload     TEXT          NOT NULL,
    published   BOOLEAN       NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    published_at TIMESTAMPTZ
);

CREATE INDEX idx_outbox_events_unpublished ON outbox_events (published, created_at);
