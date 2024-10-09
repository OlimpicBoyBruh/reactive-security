create TABLE users (
  id SERIAL PRIMARY KEY,
  username VARCHAR(500) UNIQUE NOT NULL,
  password VARCHAR(500) NOT NULL,
  role VARCHAR(500) NOT NULL,
  first_name VARCHAR(500) NOT NULL,
  last_name VARCHAR(500) NOT NULL,
  enabled BOOLEAN NOT NULL DEFAULT FALSE,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);