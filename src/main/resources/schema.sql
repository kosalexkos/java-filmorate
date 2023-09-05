DROP TABLE IF EXISTS MPA CASCADE;
DROP TABLE IF EXISTS Films_Genres CASCADE;
DROP TABLE IF EXISTS Friends CASCADE;
DROP TABLE IF EXISTS Likes CASCADE;
DROP TABLE IF EXISTS Users CASCADE;
DROP TABLE IF EXISTS Films CASCADE;
DROP TABLE IF EXISTS Genres CASCADE;




CREATE TABLE IF NOT EXISTS MPA (
                                   mpa_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                   mpa_name varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS Films (
                                    film_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                    name varchar NOT NULL,
                                    description varchar(200),
                                    release_date date,
                                    duration integer,
                                    mpa integer REFERENCES MPA (mpa_id) ON DELETE RESTRICT,
                                    CONSTRAINT valid_duration CHECK (duration > 0)
);

CREATE TABLE IF NOT EXISTS Genres (
                                    genre_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                    genre_name varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS Films_Genres (
                                    film_id integer REFERENCES Films (film_id) ON DELETE CASCADE,
                                    genre_id integer REFERENCES Genres (genre_id) ON DELETE CASCADE,
                                    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS Users (
                                    user_id integer GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                    email varchar NOT NULL,
                                    login varchar NOT NULL,
                                    name varchar NOT NULL ,
                                    birthday date NOT NULL,
                                    CONSTRAINT valid_login CHECK (login <> ' ')
);

CREATE TABLE IF NOT EXISTS Likes (
                                   film_id integer REFERENCES Films (film_id) ON DELETE CASCADE ,
                                   user_id integer REFERENCES Users (user_id) ON DELETE CASCADE ,
                                   PRIMARY KEY (user_id, film_id)
);

CREATE TABLE IF NOT EXISTS Friends (
                                    user_id integer REFERENCES Users (user_id) ON DELETE CASCADE,
                                    friend_id integer REFERENCES Users (user_id) ON DELETE CASCADE,
                                    CONSTRAINT validate_request CHECK (user_id <> friend_id),
                                    PRIMARY KEY (user_id, friend_id)
);