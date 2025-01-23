CREATE TABLE permission
(
    id BIGSERIAL UNIQUE,	    
	description character varying(50),
	access_key character varying(36) UNIQUE NOT NULL,
	CONSTRAINT pk_permission_id PRIMARY KEY (id)
);