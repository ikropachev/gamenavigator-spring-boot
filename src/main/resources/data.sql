INSERT INTO users (name, email, password, registered)
VALUES  ('admin', 'admin@gmail.com', '{noop}admin', '2021-12-02 18:00:00'),
        ('user', 'user@gmail.com', '{noop}user', '2021-12-02 18:01:00'),
        ('second_user', 'second@gmail.com', '{noop}second_pass', '2021-12-01 18:02:00'),
        ('third_user', 'third@gmail.com', '{noop}third_pass', '2021-12-01 18:03:00');

INSERT INTO genres (name)
VALUES  ('action'),
        ('rts'),
        ('adventure'),
        ('horror');

INSERT INTO game (name, developer)
VALUES  ('half-life', 'valve'),
        ('silent hill', 'konami'),
        ('warcraft', 'blizzard'),
        ('starcraft', 'blizzard');

INSERT INTO game_x_genres (game_id, genre_id)
VALUES  (1, 1),
        (1, 4),
        (2, 1),
        (2, 3),
        (2, 4),
        (3, 2),
        (4, 2);

INSERT INTO user_role (role, user_id)
VALUES ('ADMIN', 1),
       ('USER', 1),
       ('USER', 2),
       ('USER', 3),
       ('USER', 4);