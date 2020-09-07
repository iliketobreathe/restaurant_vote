package ru.iliketobreathe.restaurantvote.web.restaurant;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.iliketobreathe.restaurantvote.model.Restaurant;
import ru.iliketobreathe.restaurantvote.model.Vote;
import ru.iliketobreathe.restaurantvote.util.exception.LateVoteException;
import ru.iliketobreathe.restaurantvote.util.exception.NotFoundException;
import ru.iliketobreathe.restaurantvote.web.user.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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


    @GetMapping("/{id}/vote")
    public void vote(@PathVariable int id) throws Exception {
        Vote vote = voteRepository.getByUserIdAndDate(SecurityUtil.authUserId(), LocalDate.now());
        Restaurant restaurant = super.get(id);
        if (restaurant == null) {
            throw new NotFoundException("There is no such restaurant");
        }
        if (vote == null) {
            voteRepository.save(SecurityUtil.authUserId(), id);
        } else {
            if (LocalTime.now().isAfter(VOTE_MAX_TIME_ALLOWED)) {
                throw new LateVoteException("You are not allowed to change your vote after 11:00 A.M.");
            }
            vote.setRestaurant(super.get(id));
        }
    }
}
