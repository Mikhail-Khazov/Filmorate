package ru.yandex.practicum.filmorate.storage.feed;

import ru.yandex.practicum.filmorate.model.FeedRow;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface FeedMapper {
    static FeedRow map(ResultSet rs, long row) throws SQLException {
        return new FeedRow(rs.getLong("EVENT_ID"),
                rs.getLong("USER_ID"),
                rs.getLong("ENTITY_ID"),
                EventType.toEnum(rs.getString("EVENT_TYPE")),
                Operation.toEnum(rs.getString("OPERATION")),
                rs.getLong("TIMELONG")
        );
    }
}
