CREATE TABLE customers
(
  id          bigserial primary key,
  external_id uuid      not null,
  first_name  text      not null,
  middle_name text      not null,
  last_name   text      not null,
  gender      character not null,
  birth_date  date      not null
);

-- CREATE INDEX idx_customers_external_id ON customers(external_id);

ALTER SEQUENCE customers_id_seq OWNED BY customers.id;
