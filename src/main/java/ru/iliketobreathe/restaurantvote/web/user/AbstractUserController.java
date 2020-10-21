package ru.iliketobreathe.restaurantvote.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import ru.iliketobreathe.restaurantvote.model.User;
import ru.iliketobreathe.restaurantvote.repository.CrudUserRepository;
import ru.iliketobreathe.restaurantvote.to.UserTo;
import ru.iliketobreathe.restaurantvote.util.UserUtil;

import java.util.List;

import static ru.iliketobreathe.restaurantvote.util.UserUtil.prepareToSave;
import static ru.iliketobreathe.restaurantvote.util.ValidationUtil.*;

public abstract class AbstractUserController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private CrudUserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UniqueMailValidator emailValidator;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(emailValidator);
    }

    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    @Cacheable("users")
    public List<User> getAll() {
        log.info("getAll");
        return repository.findAll(SORT_NAME_EMAIL);
    }

    public User get(int id) {
        log.info("get {}", id);
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    public User create(UserTo userTo) {
        log.info("create from to {}", userTo);
        return create(UserUtil.createNewFromTo(userTo));
    }

    @CacheEvict(value = "users", allEntries = true)
    public User create(User user) {
        log.info("create {}", user);
        checkNew(user);
        Assert.notNull(user, "user must not be null");
        prepareToSave(user, passwordEncoder);
        return repository.save(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void delete(int id) {
        log.info("delete {}", id);
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void update(User user, int id) {
        log.info("update {} with id={}", user, id);
        Assert.notNull(user, "user must not be null");
        assureIdConsistent(user, id);
        prepareToSave(user, passwordEncoder);
        repository.save(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void update(UserTo userTo, int id) {
        log.info("update {} with id={}", userTo, id);
        Assert.notNull(userTo, "user must not be null");
        assureIdConsistent(userTo, id);
        User user = get(userTo.id());
        User updatedUser = UserUtil.updateFromTo(user, userTo);
        prepareToSave(updatedUser, passwordEncoder);
//        repository.save(updatedUser); // !! need only for JDBC implementation
    }

    public User getByMail(String email) {
        log.info("getByEmail {}", email);
        Assert.notNull(email, "email must not be null");
        return checkNotFound(repository.getByEmail(email), "email=" + email);
    }
}