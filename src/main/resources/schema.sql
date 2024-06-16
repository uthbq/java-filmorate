create table IF NOT EXISTS GENRES
(
    ID   INTEGER auto_increment,
    NAME VARCHAR(255),
    constraint GENRES_PK
    primary key (ID)
    );

create table IF NOT EXISTS MPA
(
    ID   INTEGER auto_increment,
    NAME VARCHAR(255),
    constraint MPA_PK
    primary key (ID)
    );

create table IF NOT EXISTS FILMS
(
    ID           INTEGER auto_increment,
    MPA_ID       INTEGER,
    DESCRIPTION  VARCHAR(2000),
    RELEASE_DATE DATE,
    DURATION     INTEGER,
    NAME         CHARACTER VARYING not null,
    constraint FILM_PK
    primary key (ID),
    constraint FILM_MPA_FK
    foreign key (MPA_ID) references MPA
    on update cascade on delete cascade
    );

create table IF NOT EXISTS FILM_GENRES
(
    FILM_ID  INTEGER,
    GENRE_ID INTEGER,
    constraint FILM_GENRES_FILM_FK
    foreign key (FILM_ID) references FILMS
        on update cascade on delete cascade,
    constraint FILM_GENRE_GENRES_FK
    foreign key (GENRE_ID) references GENRES
    on update cascade on delete cascade
    );

create table IF NOT EXISTS USERS
(
    ID       INTEGER auto_increment,
    BIRTHDAY DATE,
    NAME     VARCHAR(255),
    LOGIN    VARCHAR(128) not null,
    EMAIL    VARCHAR(128) not null,
    constraint USER_PK
    primary key (ID)
    );

create table IF NOT EXISTS FILM_LIKES
(
    FILM_ID INTEGER,
    USER_ID INTEGER,
    constraint FILM_LIKES_FILM_FK
    foreign key (FILM_ID) references FILMS
    on update cascade on delete cascade,
    constraint FILM_LIKES_USER_FK
    foreign key (USER_ID) references USERS
    on update cascade on delete cascade
    );

create table IF NOT EXISTS FRIENDS
(
    USER_ID   INTEGER,
    FRIEND_ID INTEGER,
    constraint FRIENDS_USER_FK
    foreign key (USER_ID) references USERS
    on update cascade on delete cascade,
    constraint FRIENDS_USER_FK_1
    foreign key (FRIEND_ID) references USERS
    on update cascade on delete cascade
    );