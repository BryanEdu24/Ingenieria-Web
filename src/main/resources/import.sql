
INSERT INTO House (id, enabled, name, pass) VALUES 
(1, TRUE, 'HOUSE_1', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W'),
(2, TRUE, 'HOUSE_2', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W'),
(3, FALSE, 'HOUSE_3', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');

INSERT INTO Room (id, enabled, name, house_id)VALUES 
(1, TRUE, 'Cocina', 1),
(2, TRUE, 'Baño', 1),
(3, TRUE, 'Habitación', 1),
(4, TRUE, 'Pasillo', 2),
(5, TRUE, 'Salón', 2),
(6, FALSE, 'Cocina', 2);

-- insert admin (username a, password aa)
INSERT INTO IWUser (id, enabled, roles, username, password, house_id) VALUES
(1, TRUE, 'ADMIN', 'Manuel', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', null),
(2, TRUE, 'MANAGER', 'Lucas', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 1),
(3, TRUE, 'USER', 'Clara', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 1),
(4, TRUE, 'USER', 'David', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 1),
(5, TRUE, 'USER', 'Edu', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 1),
(6, TRUE, 'USER', 'Fumu', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', null),
(7, TRUE, 'MANAGER', 'Carlos', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 2),
(8, TRUE, 'USER', 'Marta', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 2);


-- INSERT INTO Task (id, enabled, title, author, user_id, room_id) VALUES
-- (1, TRUE, 'Hacer la Comida', 'Lucas', 2, 1),
-- (2, TRUE, 'Limpiar', 'David', 4, 2),
-- (3, TRUE, 'Recoger', 'Clara', 2, 3);

-- start id numbering from a value that is larger than any assigned above
ALTER SEQUENCE "PUBLIC"."GEN" RESTART WITH 1024;
