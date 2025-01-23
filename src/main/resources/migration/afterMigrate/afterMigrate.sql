-----------
-- Users --
-----------
INSERT INTO user_app
	(name, cpf, email, login, password, access_key)
SELECT TMP.* FROM (
    SELECT 'Administrador' AS name, '000.000.000-00' AS cpf, 'admin@admin.com.br' AS email, 'admin' AS login, '$2a$10$ytjn5CLqRZ/1DUbnyXZ6h.kWEkeLGcbbpIrDTL3ZNV0RUT.GyILS6' AS password, 'C205684D-CAB6-4B5C-B813-AFCC77F33501' AS access_key UNION
    SELECT 'Piloto' AS name, '999.999.999-99' AS cpf, 'piloto@piloto.com.br' AS email, 'piloto' AS login, '$2a$10$5IMqFA5F9xVJTFqLDU/V5e5ZgANiHqCR7I09TH5agWa3cir5DfAsi' AS password, 'B7021EF1-C579-4A30-9A50-2F0DEEFF7BE9' AS access_key
) TMP
WHERE TMP.login NOT IN (
	SELECT u.login FROM user_app u
);

-----------------
-- Users Group --
-----------------
INSERT INTO users_group
	(code, description, user_id_insert, date_insert, date_update, access_key)
SELECT TMP.* FROM (

	SELECT 'ADM' AS code,'Administradores',(SELECT id FROM user_app WHERE access_key = 'C205684D-CAB6-4B5C-B813-AFCC77F33501'),CAST('2024-08-13 15:15:00.657192+00' AS timestamp with time zone),CAST('2024-08-13 15:15:00.657262+00' AS timestamp with time zone),'DBFBF1AB-C212-41DB-96C3-89B3AA20B4EF'

) TMP
WHERE TMP.code NOT IN (
	SELECT ug.code FROM users_group ug
);

---------------------------------------------
-- Add All permission to Users Group Admin --
---------------------------------------------
INSERT INTO users_group_permissions
    (users_group_id,permission_id)
SELECT
    ug.id as users_group_id
    , p.id as permission_id
FROM
     users_group ug
    ,  permission p
WHERE
    ug.code = 'ADM'
    AND p.id NOT IN (
        SELECT
            ugp.permission_id
        FROM
            users_group_permissions ugp
        INNER JOIN
            users_group ug ON
            ug.id = ugp.users_group_id
            AND ug.code = 'ADM'
    );