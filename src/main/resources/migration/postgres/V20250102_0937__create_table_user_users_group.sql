CREATE TABLE user_users_group
(
    id BIGSERIAL UNIQUE,
	user_id bigint,
	users_group_id bigint,
	CONSTRAINT pk_user_users_group_id PRIMARY KEY (id),
	CONSTRAINT fk_user_users_group_user_app_id FOREIGN KEY(user_id) REFERENCES user_app(id),
	CONSTRAINT fk_user_users_group_users_group_id FOREIGN KEY(users_group_id) REFERENCES users_group(id)
);