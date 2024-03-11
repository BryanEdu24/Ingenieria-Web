
INSERT INTO House (id, enabled, name, pass)
VALUES (1, TRUE, 'HOUSE_1', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');

INSERT INTO Room (id, enabled, name, house_id)
VALUES (1, TRUE, 'Cocina', 1);

INSERT INTO Room (id, enabled, name, house_id)
VALUES (2, TRUE, 'Baño', 1);

INSERT INTO Room (id, enabled, name, house_id)
VALUES (3, TRUE, 'Habitación', 1);

-- insert admin (username a, password aa)
INSERT INTO IWUser (id, enabled, roles, username, password)
VALUES (1, TRUE, 'ADMIN,USER', 'a',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');

INSERT INTO IWUser (id, enabled, roles, username, password, house_id)
VALUES (2, TRUE, 'USER', 'b',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 1);

INSERT INTO IWUser (id, enabled, roles, username, password, house_id)
VALUES (3, TRUE, 'USER', 'c',
    '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 1);

INSERT INTO Task (id, enabled, title, author, user_id, room_id)
VALUES (2, TRUE, 'Limpiar', 'b', 3, 1);

-- start id numbering from a value that is larger than any assigned above
ALTER SEQUENCE "PUBLIC"."GEN" RESTART WITH 1024;
