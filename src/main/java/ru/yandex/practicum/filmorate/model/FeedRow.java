package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class FeedRow {
    private final int eventId;
    private final int userId;
    private final int entityId;
    private final String eventType;
    private final String operation;
    private final long timestamp;
}
