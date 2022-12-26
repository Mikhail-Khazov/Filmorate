package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final LikeService likeService;
    private final FilmService filmService;

    public User get(Long userId) {
        User user = userStorage.get(userId).orElseThrow(
                () -> new UserNotFoundException("Пользователь с id: " + userId + ", не найден")
        );
        log.info("Получен пользователь id: {}", userId);
        return user;
    }

    public User create(User user) {
        nameCheck(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        if (userStorage.update(user) > 0) return user;
        else throw new UserNotFoundException("Пользователь с id: " + user.getId() + ", не найден");
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    private void nameCheck(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("UserService: пользователю с логином \"{}\", в поле \"name\" присвоино значение логина", user.getLogin());
        }
    }

    public void validateUser(Long id) {
        userStorage.get(id).orElseThrow(() -> new UserNotFoundException("Пользователь с id: " + id + ", не найден"));
        log.info("Пользователь с id: {}, есть в базе данных", id);
    }

    public void delete(Long userId) {
        if (!userStorage.delete(userId)) {
            throw new UserNotFoundException("Пользователь с id: " + userId + ", не найден");
        }
    }

    public List<Film> getRecommendations(Long userId) {
        List<Long> userLikes = likeService.getListOfLikes(userId);
        List<Film> recommendationsFilms = new ArrayList<>();
        List<Long> similarFilmsId = new ArrayList<>();
        User mostSimilarUser = new User();
        List<User> users = getAll();
        users.remove(get(userId));

        if (userLikes.size() == 0) return recommendationsFilms;

        for (User user : users) {
            List<Long> currentUserLikes = likeService.getListOfLikes(user.getId());
            currentUserLikes.retainAll(userLikes);

            if (currentUserLikes.size() > similarFilmsId.size()) {
                similarFilmsId = currentUserLikes;
                mostSimilarUser = user;
            }
        }
        if (similarFilmsId.size() == 0) return recommendationsFilms;
        similarFilmsId = likeService.getListOfLikes(mostSimilarUser.getId());
        similarFilmsId.removeAll(userLikes);

        for (long id : similarFilmsId) {
            recommendationsFilms.add(filmService.get(id));
        }
        return recommendationsFilms;
    }
}
