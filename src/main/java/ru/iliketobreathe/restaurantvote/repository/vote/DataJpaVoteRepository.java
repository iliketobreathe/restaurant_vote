package ru.iliketobreathe.restaurantvote.repository.vote;

import org.springframework.stereotype.Repository;
import ru.iliketobreathe.restaurantvote.model.Vote;
import ru.iliketobreathe.restaurantvote.repository.restaurant.DataJpaRestaurantRepository;
import ru.iliketobreathe.restaurantvote.repository.user.DataJpaUserRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaVoteRepository {

    private final CrudVoteRepository crudRepository;
    private final DataJpaRestaurantRepository restaurantRepository;
    private final DataJpaUserRepository userRepository;

    public DataJpaVoteRepository(CrudVoteRepository crudRepository, DataJpaRestaurantRepository restaurantRepository, DataJpaUserRepository userRepository) {
        this.crudRepository = crudRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    public Vote save(int userId, int restaurantId) {
        Vote vote = new Vote();
        vote.setRestaurant(restaurantRepository.get(restaurantId));
        vote.setUser(userRepository.get(userId));
        return crudRepository.save(vote);
    }

    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    public Vote get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    public Vote getByUserIdAndDate(int userId, LocalDate date) {
        return crudRepository.findByUserIdAndDate(userId, date);
    }

    public List<Vote> getAll() {
        return crudRepository.findAll();
    }

    public List<Vote> getAllByDate(LocalDate date) {
        return crudRepository.findByDate(date);
    }

    public List<Vote> getAllByRestaurantIdAndDate(int restaurantId, LocalDate date) {
        return crudRepository.findByRestaurantIdAndDate(restaurantId, date);
    }
}

