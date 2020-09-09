package ru.iliketobreathe.restaurantvote;

import ru.iliketobreathe.restaurantvote.model.Dish;

import static ru.iliketobreathe.restaurantvote.RestaurantTestData.REST_1;
import static ru.iliketobreathe.restaurantvote.RestaurantTestData.REST_2;
import static ru.iliketobreathe.restaurantvote.model.AbstractBaseEntity.START_SEQ;

public class DishTestData {
    public static TestMatcher<Dish> DISH_MATCHER = TestMatcher.usingEqualsAssertions(Dish.class);

    public static final int NOT_FOUND = 10;
    public static final int DISH_1_ID = START_SEQ + 4;
    public static final int DISH_2_ID = START_SEQ + 5;

    public static final Dish DISH_1 = new Dish(100004, "Blue crab", 70, REST_1);
    public static final Dish DISH_2 = new Dish(100005, "Crab", 50, REST_1);

    public static Dish getNew() {
        return new Dish(null, "New Dish", 999, REST_1);
    }

    public static Dish getUpdated() {
        Dish updated = new Dish(DISH_1);
        updated.setName("UpdatedName");
        updated.setPrice(2);
        updated.setRestaurant(REST_2);
        return updated;
    }
}
