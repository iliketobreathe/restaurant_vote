package ru.iliketobreathe.restaurantvote.repository.dish;

import org.springframework.stereotype.Repository;
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

    public Dish save(Dish dish, int restaurantId) {
        if (!dish.isNew() && get(dish.getId(), restaurantId) == null) {
            return null;
        }
        dish.setRestaurant(crudRestaurantRepository.getOne(restaurantId));
        return crudRepository.save(dish);
    }

    public Dish get(int id, int restaurantId) {
        return crudRepository.findById(id)
                .filter(dish -> dish.getRestaurant().getId() == restaurantId)
                .orElse(null);
    }

    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    public Dish get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    public List<Dish> getAll(int restaurantId, LocalDate date) {
        return crudRepository.findAllByRestaurantIdAndDateOrderByName(restaurantId, date);
    }
}

