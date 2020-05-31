CREATE TABLE game (
    game_id serial PRIMARY KEY,
    user_id int NOT NULL,
    game_body text NOT NULL,
	created_datetime TIMESTAMP NOT NULL,
    modified_datetime TIMESTAMP NOT NULL
);
