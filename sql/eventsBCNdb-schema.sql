drop database if exists eventsBCNdb;
create database eventsBCNdb;

use eventsBCNdb;

CREATE TABLE users (
    id BINARY(16) NOT NULL,
    name VARCHAR(15) NOT NULL UNIQUE,
    password BINARY(16) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    photo VARCHAR(800) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE companies (
    id BINARY(16) NOT NULL,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(400) NOT NULL,
    location VARCHAR(250) NOT NULL,
    latitude FLOAT(10,6),
    longitude FLOAT(10,6),
    userid BINARY(16) NOT NULL,
    FOREIGN KEY (userid) REFERENCES users(id) on delete cascade,
    PRIMARY KEY (id)
);

CREATE TABLE r_users (
    referenceid BINARY(16) NOT NULL,
    followerid BINARY(16) NOT NULL,
    FOREIGN KEY (referenceid) REFERENCES users(id) on delete cascade,
    FOREIGN KEY (followerid) REFERENCES users(id) on delete cascade,
    PRIMARY KEY (referenceid, followerid)
);

CREATE TABLE user_roles (
    userid BINARY(16) NOT NULL,
    role VARCHAR(15) NOT NULL,
    FOREIGN KEY (userid) REFERENCES users(id) on delete cascade,
    PRIMARY KEY (userid, role)
);

CREATE TABLE auth_tokens (
    userid BINARY(16) NOT NULL,
    token BINARY(16) NOT NULL,
    FOREIGN KEY (userid) REFERENCES users(id) on delete cascade,
    PRIMARY KEY (token)
);

CREATE TABLE categories_user (
    userid BINARY(16) NOT NULL,
    category VARCHAR(15) NOT NULL,
    FOREIGN KEY (userid) REFERENCES users(id) on delete cascade,
    PRIMARY KEY (userid, category)
);

CREATE TABLE events (
    id BINARY(16) NOT NULL,
    title VARCHAR(50) NOT NULL,
    description VARCHAR(400) NOT NULL,
    date DATETIME NOT NULL,
    photo VARCHAR(800) NOT NULL,
    category VARCHAR(16) NOT NULL,
    companyid BINARY(16) NOT NULL,
    last_modified TIMESTAMP NOT NULL,
    creation_timestamp DATETIME not null default current_timestamp,
    FOREIGN KEY (companyid) REFERENCES companies(id) on delete cascade,
    PRIMARY KEY (id)
);

CREATE TABLE user_event (
    eventid BINARY(16) NOT NULL,
    userid BINARY(16) NOT NULL,
    FOREIGN KEY (eventid) REFERENCES events(id) on delete cascade,
    FOREIGN KEY (userid) REFERENCES users(id) on delete cascade,
    PRIMARY KEY (eventid, userid)
);
