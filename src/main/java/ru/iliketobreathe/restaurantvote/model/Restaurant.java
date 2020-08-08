package ru.iliketobreathe.restaurantvote.model;

import java.util.List;

public class Restaurant extends AbstractNamedEntity {
    private List<Dish> dishes;

    public Restaurant() {
    }

    public Restaurant(Integer id, String name) {
        super(id, name);
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }
}
