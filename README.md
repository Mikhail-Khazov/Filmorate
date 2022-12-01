# java-filmorate
Template repository for Filmorate project.
![Diagram] (/diagram.png)

Извлечение названий фильмов определённого жанра:
```
SELECT f.Title
FROM Genre AS g
LEFT JOIN FilmGenre AS fg ON g.Genre_id = fg.Genre_id
LEFT JOIN Film AS f ON  fg.Film_id = f.Film_id
WHERE g.Title LIKE '%ACTION'
```

Извлечение названий фильмов определённого возростного рейтинга:
```
SELECT f.Title
FROM Rating AS r
LEFT JOIN Film AS f ON r.MPAA_Rating_id = f.MPAA_Rating_id
WHERE r.Rating LIKE '%PG'
```

Извлечение друзей пользователя c id = 1
```
SELECT f.Friend_id, u.Name
FROM Friends AS f
LEFT JOIN User AS u ON f.Friend_id = u.User_id
WHERE f.User_id = 1 AND f.Mutual = true
```