DROP TABLE IF EXISTS review;
DROP TABLE IF EXISTS client;

CREATE TABLE client
(
id SERIAL PRIMARY KEY,
api_key varchar,
name VARCHAR,
email varchar
);

CREATE TABLE review
(
id SERIAL PRIMARY KEY,
client_id INTEGER,
product_name VARCHAR,
comment varchar,
ratings INTEGER,
review_key VARCHAR,
status VARCHAR,
user_name VARCHAR,
FOREIGN KEY (client_id) REFERENCES client(id)
);