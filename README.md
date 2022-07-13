# java-filmorate
Filmorate project
![img_1.png](img_1.png)
Пример запросов к баззе данных:

Получить всех пользователей:
SELECT * 
FROM user

Получить пользователя по id
SELECT *
FROM user
WHERE id = n

Получить фильм по идентификатору:
SELECT * 
FROM film
WHERE id = n

Получить первые n популярных фильмов
SELECT * ,
COUNT(user_id) 
FROM film
RIGHT JOIN likes ON film.id = likes.film_id
GROUP BY film_id
ORDER BY user_id
LIMIT n 