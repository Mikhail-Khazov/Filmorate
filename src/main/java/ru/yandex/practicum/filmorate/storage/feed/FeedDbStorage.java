package ru.yandex.practicum.filmorate.storage.feed;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.FeedRow;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.RowMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class FeedDbStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserService userService;

    //Данные ленты новостей для одного пользователя
    public List<FeedRow> getByUserId(int idUser) {
        log.info("Запрос на получение ленты новостей пользователя id=" + idUser);
        userService.get(idUser);

        String sqlQuery = "SELECT * " +
                "FROM feed " +
                "WHERE feed.USER_ID = ? ";
        return jdbcTemplate.query(sqlQuery, RowMapper::mapRowToFeedRow, idUser);
    }

    public int getIdAuthor(int idReview) {
        final String sqlQuery = String.format("SELECT USER_ID " +
                "FROM feed " +
                "WHERE EVENT_TYPE='REVIEW' AND OPERATION='ADD' AND ENTITY_ID=? ");

        return jdbcTemplate.queryForObject(sqlQuery, Integer.class, idReview);
    }
}
