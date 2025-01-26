CREATE TABLE bucket_file (
    id BIGSERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    content_type VARCHAR(255) NOT NULL,
    size BIGINT NOT NULL,
    url VARCHAR(255) NOT NULL,
    description VARCHAR(255)
);