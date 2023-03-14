package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.yandex.practicum.filmorate.storage.feed.EventType;
import ru.yandex.practicum.filmorate.storage.feed.Operation;

@AllArgsConstructor
@Getter
public class FeedRow {
    private final Long eventId;
    private final Long userId;
    private final Long entityId;
    private final EventType eventType;
    private final Operation operation;
    private final long timestamp;
}
