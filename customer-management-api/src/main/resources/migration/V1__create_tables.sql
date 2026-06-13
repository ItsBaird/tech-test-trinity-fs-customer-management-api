-- =============================================
-- DDL - Creación de tablas
-- V1__create_tables.sql
-- =============================================

CREATE TABLE IF NOT EXISTS public.customers
(
    id                    SERIAL                      NOT NULL,
    identification_type   CHARACTER VARYING           NOT NULL,
    identification_number CHARACTER VARYING(25)       NOT NULL,
    names                 CHARACTER VARYING           NOT NULL,
    surnames              CHARACTER VARYING           NOT NULL,
    email                 CHARACTER VARYING           NOT NULL,
    date_of_birth         DATE                        NOT NULL,
    created_at            TIMESTAMP WITH TIME ZONE    NOT NULL,
    modified_at           TIMESTAMP WITH TIME ZONE,
    CONSTRAINT customer_id_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.accounts
(
    id             BIGSERIAL                   NOT NULL,
    account_type   CHARACTER VARYING(20)       NOT NULL,
    account_number BIGINT                      NOT NULL,
    account_state  CHARACTER VARYING(20)       NOT NULL,
    balance        NUMERIC(18,2)               NOT NULL,
    gmf_exempt     BOOLEAN                     NOT NULL,
    created_at     TIMESTAMP WITH TIME ZONE    NOT NULL,
    modified_at    TIMESTAMP WITH TIME ZONE,
    customer_id    BIGINT                      NOT NULL,
    CONSTRAINT account_id_pk PRIMARY KEY (id),
    CONSTRAINT customer_id_fk FOREIGN KEY (customer_id)
        REFERENCES public.customers (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS public.transactions
(
    id                     BIGSERIAL                   NOT NULL,
    transaction_type       CHARACTER VARYING           NOT NULL,
    amount                 NUMERIC(18,2)               NOT NULL,
    source_account_id      BIGINT,
    destination_account_id BIGINT,
    transaction_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT transactions_id_pk PRIMARY KEY (id),
    CONSTRAINT source_account_id_fk FOREIGN KEY (source_account_id)
        REFERENCES public.accounts (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT destination_account_id_fk FOREIGN KEY (destination_account_id)
        REFERENCES public.accounts (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);