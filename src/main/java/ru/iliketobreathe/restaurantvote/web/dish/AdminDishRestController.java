package ru.iliketobreathe.restaurantvote.web.dish;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.iliketobreathe.restaurantvote.model.Dish;
import ru.iliketobreathe.restaurantvote.repository.dish.DataJpaDishRepository;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.iliketobreathe.restaurantvote.util.ValidationUtil.*;

@RestController
@RequestMapping(value = AdminDishRestController.REST_URL)
public class AdminDishRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    static final String REST_URL = "/rest/admin/dishes";

    DataJpaDishRepository repository;

    public AdminDishRestController(DataJpaDishRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/{restaurantId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Cacheable("dishes")
    public List<Dish> getAll(@PathVariable("restaurantId") int restaurantId) {
        log.info("getAll");
        return repository.getAll(restaurantId, LocalDate.now());
    }

    @GetMapping(value = "/{restaurantId}/dish/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Dish get(@PathVariable int id, @PathVariable("restaurantId") int restaurantId) {
        log.info("get {}", id);
        return checkNotFoundWithId(repository.get(id, restaurantId), id);
    }

    @DeleteMapping("/{restaurantId}/dish/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "dishes", allEntries = true)
    public void delete(@PathVariable int id, @PathVariable int restaurantId) {
        repository.delete(id, restaurantId);
    }

    @PostMapping(value = "{restaurantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(value = "dishes", allEntries = true)
    public ResponseEntity<Dish> createWithLocation(@RequestBody Dish dish, @PathVariable int restaurantId) {
        log.info("create {}", dish);
        checkNew(dish);
        Assert.notNull(dish, "dish must not be null");
        Dish created = repository.save(dish, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/dishes/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{restaurantId}/dish/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants", allEntries = true)
    @Transactional
    public void update(@RequestBody Dish dish, @PathVariable int id, @PathVariable int restaurantId) {
        log.info("update {} with id={}", dish, id);
        Assert.notNull(dish, "dish must not be null");
        assureIdConsistent(dish, id);
        repository.save(dish, restaurantId);
    }
}
