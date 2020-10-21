package ru.iliketobreathe.restaurantvote.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import ru.iliketobreathe.restaurantvote.model.Restaurant;
import ru.iliketobreathe.restaurantvote.repository.CrudRestaurantRepository;
import ru.iliketobreathe.restaurantvote.repository.vote.DataJpaVoteRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.iliketobreathe.restaurantvote.util.ValidationUtil.checkNotFoundWithId;

public abstract class AbstractRestaurantController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    CrudRestaurantRepository repository;

    @Autowired
    DataJpaVoteRepository voteRepository;

    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");


    public List<Restaurant> getAll() {
        log.info("getAll");
        return repository.findAll(SORT_NAME);
    }

    @Cacheable("restaurants")
    public List<Restaurant> getAllWithDishes(LocalDate date) {
        log.info("getAllWithDishes");
        return repository.getAllWithDishes(date);
    }

    public Restaurant get(int id) {
        log.info("get {}", id);
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    public Restaurant getWithDishes(int id, LocalDate date) {
        log.info("get {} with dishes", id);
        return checkNotFoundWithId(repository.getWithDishes(id, date), id);
    }
}
