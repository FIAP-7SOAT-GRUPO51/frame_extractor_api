CREATE TABLE users_group_permissions
(
    id BIGINT AUTO_INCREMENT NOT NULL,
	users_group_id bigint,
	permission_id bigint,
	CONSTRAINT pk_users_group_permissions_id PRIMARY KEY (id),
	CONSTRAINT fk_users_group_permissions_users_group_id FOREIGN KEY(users_group_id) REFERENCES users_group(id),
	CONSTRAINT fk_users_group_permissions_permission_id FOREIGN KEY(permission_id) REFERENCES permission(id)
);