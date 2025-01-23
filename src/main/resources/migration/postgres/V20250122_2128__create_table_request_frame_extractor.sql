CREATE TABLE request_frame_extractor
(	
	id BIGSERIAL UNIQUE,
    description character varying(250)  NOT NULL,
    file_name character varying(250) NOT NULL,
	fps int DEFAULT 1,
	status character varying(16) NOT NULL,
	user_id_insert bigint NULL,
    date_insert timestamp with time zone NOT NULL,
    user_id_update bigint NULL,
    date_update timestamp with time zone NULL,
    access_key character varying(36) UNIQUE NOT NULL,
    CONSTRAINT pk_request_frame_extractor_id PRIMARY KEY (id),
    CONSTRAINT fk_request_frame_extractor_user_app_insert_audit FOREIGN KEY(user_id_insert) REFERENCES user_app(id),
    CONSTRAINT fk_request_frame_extractor_user_app_update_audit FOREIGN KEY(user_id_update) REFERENCES user_app(id)
);