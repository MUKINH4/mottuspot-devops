DROP TABLE dispositivo CASCADE;

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE dispositivo (
     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     ativo BOOLEAN NOT NULL,
     moto_id BIGINT
);

UPDATE moto SET dispositivo_id = NULL;

ALTER TABLE moto
    ALTER COLUMN dispositivo_id TYPE uuid USING dispositivo_id::uuid;

ALTER TABLE moto
    ADD CONSTRAINT fk_moto_on_dispositivo
        FOREIGN KEY (dispositivo_id) REFERENCES dispositivo(id);
