-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    street VARCHAR(255),
    suite VARCHAR(255),
    city VARCHAR(255),
    zipcode VARCHAR(255),
    geo_lat VARCHAR(255),
    geo_lng VARCHAR(255),
    phone VARCHAR(255),
    website VARCHAR(255),
    company_name VARCHAR(255),
    company_catch_phrase VARCHAR(255),
    company_bs VARCHAR(255)
);

-- Create auth_users table
CREATE TABLE auth_users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL
);

-- Create indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_auth_users_email ON auth_users(email); 