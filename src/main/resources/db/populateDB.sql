DELETE FROM user_roles;
DELETE FROM votes;
DELETE FROM dishes;
DELETE FROM restaurants;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO restaurants (name, id)
VALUES ('LAGUNA', 100002),
       ('South Star', 100003);

INSERT INTO dishes (name, price, date, restaurant_id)
VALUES ('Blue crab', 70, today(), 100002),
       ('Crab', 50, today(), 100002);


INSERT INTO votes (date, user_id, restaurant_id)
VALUES ('2020-01-30', 100000, 100002),
       ('2020-01-31', 100001, 100002),
       ('2020-01-28', 100001, 100003);