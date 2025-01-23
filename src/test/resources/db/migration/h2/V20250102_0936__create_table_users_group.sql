CREATE TABLE users_group
(	
	id BIGINT AUTO_INCREMENT NOT NULL,
	code VARCHAR(6) UNIQUE NOT NULL,
	description VARCHAR(100) NOT NULL,
    user_id_insert bigint NULL,
    date_insert timestamp with time zone NOT NULL,
    user_id_update bigint NULL,
    date_update timestamp with time zone NULL,
    access_key VARCHAR(36) UNIQUE NOT NULL,
    CONSTRAINT pk_users_group_id PRIMARY KEY (id),
    CONSTRAINT fk_users_group_user_app_insert_audit FOREIGN KEY(user_id_insert) REFERENCES user_app(id),
    CONSTRAINT fk_users_group_user_app_update_audit FOREIGN KEY(user_id_update) REFERENCES user_app(id)
);