DELETE FROM "USERS";
ALTER TABLE "USERS" ALTER COLUMN ID RESTART WITH 1;
DELETE FROM "FILMS";
ALTER TABLE "FILMS" ALTER COLUMN ID RESTART WITH 1;

DELETE FROM "FRIENDS";
DELETE FROM "FILM_LIKES";

DELETE FROM "GENRES";
INSERT INTO GENRES (id, name)
VALUES (1, 'Комедия'),
       (2, 'Драма'),
       (3, 'Мультфильм'),
       (4, 'Триллер'),
       (5, 'Документальный'),
       (6, 'Боевик');


DELETE FROM "MPA";
INSERT INTO  MPA (id, name)
VALUES (1, 'G'),
       (2, 'PG'),
       (3, 'PG-13'),
       (4, 'R'),
       (5, 'NC-17');