-- Создание таблицы customers
CREATE TABLE customers (
    customer_id SERIAL PRIMARY KEY,
    chat_id BIGINT NOT NULL,
    username VARCHAR(255),
    UNIQUE (chat_id)
);

-- Создание таблицы user_request_limits
CREATE TABLE user_request_limits (
    user_id SERIAL PRIMARY KEY,
    requests_today INT NOT NULL DEFAULT 0,
    last_request_date DATE NOT NULL,
    customer_id BIGINT NOT NULL,
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customers (customer_id) ON DELETE CASCADE
);
