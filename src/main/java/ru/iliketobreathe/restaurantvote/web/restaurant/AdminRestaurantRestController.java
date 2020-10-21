package ru.iliketobreathe.restaurantvote.web.restaurant;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.iliketobreathe.restaurantvote.model.Restaurant;
import ru.iliketobreathe.restaurantvote.model.Vote;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.iliketobreathe.restaurantvote.util.ValidationUtil.*;

@RestController
@RequestMapping(AdminRestaurantRestController.REST_URL)
public class AdminRestaurantRestController extends AbstractRestaurantController {
    static final String REST_URL = "/rest/admin/restaurants";

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Restaurant> getAll(@RequestParam(value = "withDishes", required = false, defaultValue = "true") boolean withDishes) {
        if (withDishes) {
            return super.getAllWithDishes(LocalDate.now());
        }
        return super.getAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Restaurant get(@PathVariable int id, @RequestParam(value = "withDishes", required = false, defaultValue = "true") boolean withDishes) {
        if (withDishes) {
            return super.getWithDishes(id, LocalDate.now());
        }
        return super.get(id);
    }

    @GetMapping(value = "/votes", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Vote> getAllVotes(@RequestParam(name = "date", required = false) String date) {
        if (date == null) {
            return voteRepository.getAllByDate(LocalDate.now());
        }
        return voteRepository.getAllByDate(LocalDate.parse(date));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants", allEntries = true)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        checkNotFoundWithId(repository.delete(id) != 0, id);
    }

    @DeleteMapping("/votes/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants", allEntries = true)
    public void deleteVote(@PathVariable int id) {
        log.info("delete {}", id);
        checkNotFoundWithId(voteRepository.delete(id), id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @CacheEvict(value = "restaurants", allEntries = true)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        Assert.notNull(restaurant, "restaurant must not be null");
        Restaurant created = repository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @CacheEvict(value = "restaurants", allEntries = true)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update {} with id={}", restaurant, id);
        Assert.notNull(restaurant, "restaurant must not be null");
        assureIdConsistent(restaurant, id);
        repository.save(restaurant);
    }
}
