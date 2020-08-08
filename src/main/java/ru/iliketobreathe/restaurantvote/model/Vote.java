package ru.iliketobreathe.restaurantvote.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Vote extends AbstractBaseEntity {
    private Restaurant restaurant;

    private User user;

    private LocalDate localDate;

    public Vote() {
    }

    public Vote(Integer id, Restaurant restaurant, LocalDate localDate) {
        super(id);
        this.restaurant = restaurant;
        this.localDate = localDate;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public User getUser() {
        return user;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }
}
