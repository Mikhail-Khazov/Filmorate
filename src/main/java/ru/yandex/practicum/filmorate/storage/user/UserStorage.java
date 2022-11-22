package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;

public interface UserStorage {

    User create(User user);

    public User update(User user);

    User get (int userId);

    public List<User> getAll();
}
