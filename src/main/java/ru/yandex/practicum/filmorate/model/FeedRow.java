package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.yandex.practicum.filmorate.storage.feed.EventType;
import ru.yandex.practicum.filmorate.storage.feed.Operation;

@AllArgsConstructor
@Getter
public class FeedRow {
    private final long eventId;
    private final long userId;
    private final long entityId;
    private final EventType eventType;
    private final Operation operation;
    private final long timestamp;
}
