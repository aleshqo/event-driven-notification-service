ALTER TABLE account_transactions
    ALTER COLUMN amount TYPE NUMERIC(19, 2) USING amount::numeric;

ALTER TABLE account_transactions
    ALTER COLUMN direction TYPE VARCHAR(32);
