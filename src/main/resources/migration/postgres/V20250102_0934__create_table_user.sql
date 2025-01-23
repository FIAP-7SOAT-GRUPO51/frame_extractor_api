CREATE TABLE user_app
(	
	id BIGSERIAL UNIQUE,
    name character varying(100) NOT NULL,
    email character varying(100),
    cpf character varying(14) UNIQUE NOT NULL,
    login character varying(100) UNIQUE NOT NULL,
	password character varying(100) NOT NULL,
	active int DEFAULT 0,
	access_key character varying(36) UNIQUE NOT NULL,
    CONSTRAINT pk_user_app_id PRIMARY KEY (id)
);