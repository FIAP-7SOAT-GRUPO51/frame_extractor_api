CREATE TABLE permission
(
    id BIGINT AUTO_INCREMENT NOT NULL,	    
	description VARCHAR(50),
	access_key VARCHAR(36) UNIQUE NOT NULL,
	CONSTRAINT pk_permission_id PRIMARY KEY (id)
);