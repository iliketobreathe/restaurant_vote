package ru.iliketobreathe.restaurantvote.repository.dish;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.iliketobreathe.restaurantvote.model.Dish;
import ru.iliketobreathe.restaurantvote.repository.restaurant.CrudRestaurantRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DataJpaDishRepository {

    private final CrudDishRepository crudRepository;
    private final CrudRestaurantRepository crudRestaurantRepository;

    public DataJpaDishRepository(CrudDishRepository crudRepository, CrudRestaurantRepository crudRestaurantRepository) {
        this.crudRepository = crudRepository;
        this.crudRestaurantRepository = crudRestaurantRepository;
    }

    @Transactional
    public Dish save(Dish dish, int restaurantId) {
        if (!dish.isNew() && get(dish.getId(), restaurantId) == null) {
            return null;
        } else if (!dish.isNew()) {
            dish.setRestaurant(crudRestaurantRepository.getOne(restaurantId));
        }
        return crudRepository.save(dish);
    }

    public Dish get(int id, int restaurantId) {
        return crudRepository.findById(id)
                .filter(dish -> dish.getRestaurant().getId() == restaurantId)
                .orElse(null);
    }

    public boolean delete(int id, int restaurantId) {
        if(get(id, restaurantId) != null) {
            return crudRepository.delete(id) != 0;
        }
        return false;
    }

    public List<Dish> getAll(int restaurantId, LocalDate date) {
        return crudRepository.findAllByRestaurantIdAndDateOrderByName(restaurantId, date);
    }
}

