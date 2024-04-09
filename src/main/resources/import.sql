
-- House
INSERT INTO House (id, enabled, name, pass) VALUES 
(1, TRUE, 'HOUSE_1', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W'),
(2, TRUE, 'HOUSE_2', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W'),
(3, FALSE, 'HOUSE_3', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W');

-- Room
INSERT INTO Room (id, enabled, img, name, house_id)VALUES 
(1, TRUE, 'cocina', 'Cocina', 1),
(2, TRUE, 'cuarto-de-bano', 'Baño', 1),
(3, TRUE, 'room', 'Habitación', 1),
(4, TRUE, 'pasillo', 'Pasillo', 2),
(5, TRUE, 'livingroom', 'Salón', 2),
(6, FALSE, 'cocina', 'Cocina', 2);

-- User (IWUser)
INSERT INTO IWUser (id, enabled, roles, username, password, email, house_id) VALUES
(1, TRUE, 'ADMIN', 'Manuel', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'manuel.freire@fdi.ucm.es', null),
(2, TRUE, 'MANAGER', 'Lucas', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'lucbravo@ucm.es', 1),
(3, TRUE, 'USER', 'Clara', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'clarasot@ucm.es', 1),
(4, TRUE, 'USER', 'David', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'davichap@ucm.es', 1),
(5, TRUE, 'USER', 'Edu', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'bcordoba@ucm.es', 1),
(6, TRUE, 'USER', 'Fumu', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'fumufumu@ucm.es', null),
(7, TRUE, 'MANAGER', 'Carlos', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'carlcarl@ucm.es', 2),
(8, TRUE, 'USER', 'Marta', '{bcrypt}$2a$10$2BpNTbrsarbHjNsUWgzfNubJqBRf.0Vz9924nRSHBqlbPKerkgX.W', 'martmart@ucm.es', 2);

-- Task
INSERT INTO Task (id, enabled, title, author, user_id, room_id) VALUES -- Falta creationDate
(1, TRUE, 'Hacer la Comida', 'Lucas', 2, 1),
(2, TRUE, 'Limpiar', 'Lucas', 4, 2),
(3, TRUE, 'Recoger', 'Lucas', 3, 3),
(4, TRUE, 'Hacer la cama', 'Lucas', 5, 3);

-- Note

-- Notification

-- Historical

-- Expense

-- UserExpense

-- start id numbering from a value that is larger than any assigned above
ALTER SEQUENCE "PUBLIC"."GEN" RESTART WITH 1024;
