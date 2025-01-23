CREATE TABLE user_app
(	
	id BIGINT AUTO_INCREMENT NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    cpf VARCHAR(14) UNIQUE NOT NULL,
    login VARCHAR(100) UNIQUE NOT NULL,
	password VARCHAR(100) NOT NULL,
	active int DEFAULT 0,
	access_key VARCHAR(36) UNIQUE NOT NULL,
    CONSTRAINT pk_user_app_id PRIMARY KEY (id)
);