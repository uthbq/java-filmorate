# java-filmorate
Template repository for Filmorate project.
- ### Получение всех фильмов:

```SQL
SELECT *
FROM films;
```

- ### Получение всех пользователей:

```SQL
SELECT *
FROM users;   
```

-  ### Получение друзей пользователя:

```SQL
SELECT friend_id
FROM friends
WHERE user_id = ?
```

-  ### Получение топа N наиболее популярных фильмов:

```SQL
SELECT f.name AS name,
       COUNT(fl.film_id) AS count_likes
FROM films AS f
LEFT JOIN film_likes AS fl ON fl.film_id = f.id
GROUP BY f.name
ORDER BY count_likes DESC
LIMIT ?;
```

-  ### Получение жанров фильма:

```SQL
SELECT fg.genre_id AS id,
       g.name AS name
FROM film_genres AS fg
INNER JOIN genres AS g ON fg.genre_id = g.ID
WHERE fg.film_id = ?
ORDER BY fg.genre_id;
```