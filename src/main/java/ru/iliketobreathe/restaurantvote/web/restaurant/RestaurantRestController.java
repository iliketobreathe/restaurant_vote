package ru.iliketobreathe.restaurantvote.web.restaurant;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.iliketobreathe.restaurantvote.model.Restaurant;
import ru.iliketobreathe.restaurantvote.model.Vote;
import ru.iliketobreathe.restaurantvote.util.exception.LateVoteException;

import javax.transaction.Transactional;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.iliketobreathe.restaurantvote.util.ValidationUtil.assureIdConsistent;
import static ru.iliketobreathe.restaurantvote.util.ValidationUtil.checkNew;
import static ru.iliketobreathe.restaurantvote.web.user.SecurityUtil.authUserId;

@RestController
@RequestMapping(RestaurantRestController.REST_URL)
public class RestaurantRestController extends AbstractRestaurantController {
    static final String REST_URL = "/rest/profile/restaurants";
    private static final LocalTime VOTE_MAX_TIME_ALLOWED = LocalTime.of(11, 0);

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


    @GetMapping("/get_vote")
    public Vote getVote() {
        return voteRepository.getByUserIdAndDate(authUserId(), LocalDate.now());
    }

    @PostMapping(value = "/{restaurantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> vote(@RequestBody Vote vote, @PathVariable int restaurantId) throws Exception {
        log.info("create {}", vote);
        checkNew(vote);
        Assert.notNull(vote, "vote must not be null");
        if (authUserId() != vote.getUser().id()) {
            throw new IllegalArgumentException("vote doesn't contain information about your user");
        }
        Vote created = voteRepository.save(vote, restaurantId);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/get_vote")
                .build().toUri()
                ;
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{restaurantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void voteUpdate(@RequestBody Vote vote, @PathVariable int restaurantId) throws Exception{
        log.info("update {}", vote);
        assureIdConsistent(vote, getVote().id());
        if (authUserId() != vote.getUser().id()) {
            throw new IllegalArgumentException("vote doesn't contain information about your user");
        } else if (LocalTime.now().isAfter(VOTE_MAX_TIME_ALLOWED)) {
            throw new LateVoteException("You are not allowed to change your vote after" + VOTE_MAX_TIME_ALLOWED);
        }
        voteRepository.save(vote, restaurantId);
    }

}
