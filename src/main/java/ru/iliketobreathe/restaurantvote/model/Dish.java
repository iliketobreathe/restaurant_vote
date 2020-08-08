package ru.iliketobreathe.restaurantvote.model;

import java.time.LocalDate;

public class Dish extends AbstractNamedEntity {
    private int price;

    private LocalDate localDate;

    private Restaurant restaurant;

    public Dish() {
    }

    public Dish(Integer id, String name, int price, LocalDate localDate) {
        super(id, name);
        this.price = price;
        this.localDate = localDate;
    }

    public int getPrice() {
        return price;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }
}
