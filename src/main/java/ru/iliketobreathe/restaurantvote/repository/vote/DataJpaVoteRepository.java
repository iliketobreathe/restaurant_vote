package ru.iliketobreathe.restaurantvote.repository.vote;

import org.springframework.stereotype.Repository;
import ru.iliketobreathe.restaurantvote.model.Vote;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaVoteRepository {

    private final CrudVoteRepository crudRepository;

    public DataJpaVoteRepository(CrudVoteRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    public Vote save(Vote vote) {
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

