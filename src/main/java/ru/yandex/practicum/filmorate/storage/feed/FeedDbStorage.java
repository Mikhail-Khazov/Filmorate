package ru.yandex.practicum.filmorate.storage.feed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FeedRow;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FeedDbStorage implements FeedMapper {
    private final JdbcTemplate jdbcTemplate;
    private final UserService userService;

    //Данные ленты новостей для одного пользователя
    public List<FeedRow> getByUserId(Long idUser) {
        final String sqlQuery = "SELECT * " +
                "FROM feed " +
                "WHERE feed.USER_ID = ? ";
        log.info("Запрос на получение ленты новостей пользователя id=" + idUser);
        userService.get(idUser);
        return jdbcTemplate.query(sqlQuery, FeedMapper::map, idUser);
    }

    public Long getIdAuthor(Long idReview) {
        final String sqlQuery = "SELECT USER_ID " +
                "FROM feed " +
                "WHERE EVENT_TYPE = 'REVIEW' AND OPERATION = 'ADD' AND ENTITY_ID = ? ";
        return jdbcTemplate.queryForObject(sqlQuery, Long.class, idReview);
    }
}
