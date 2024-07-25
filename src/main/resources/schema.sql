DROP TABLE IF EXISTS users, friends, likes, ratings, films, film_to_genre, genres CASCADE;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username VARCHAR(40) NOT NULL,
    email VARCHAR(200) NOT NULL CHECK (email LIKE '%@%'),
    login VARCHAR(40) NOT NULL,
    name VARCHAR(40) NOT NULL,
    birthday DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS friends (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    friend_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE(user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS ratings (
    id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(40) NOT NULL
);


CREATE TABLE IF NOT EXISTS films (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(40) NOT NULL,
    description VARCHAR(200) NOT NULL,
    release_date DATE NOT NULL CHECK (release_date >= '1895-12-28'),
    duration BIGINT NOT NULL,
    rating_id int REFERENCES ratings(id)
);

CREATE TABLE IF NOT EXISTS likes (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    film_id BIGINT NOT NULL REFERENCES films(id) ON DELETE CASCADE,
    UNIQUE(user_id, film_id)
);


CREATE TABLE IF NOT EXISTS genres (
    id int GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(40) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS film_to_genre (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    film_id BIGINT NOT NULL REFERENCES films(id) ON DELETE CASCADE,
    genre_id int NOT NULL REFERENCES genres(id),
    UNIQUE(film_id, genre_id)
);