package ru.iliketobreathe.restaurantvote;

import ru.iliketobreathe.restaurantvote.model.Restaurant;

import java.util.List;

import static ru.iliketobreathe.restaurantvote.DishTestData.DISH_1;
import static ru.iliketobreathe.restaurantvote.DishTestData.DISH_2;
import static ru.iliketobreathe.restaurantvote.model.AbstractBaseEntity.START_SEQ;

public class RestaurantTestData {
    public static TestMatcher<Restaurant> RESTAURANT_MATCHER = TestMatcher.usingEqualsAssertions(Restaurant.class);

    public static final int NOT_FOUND = 10;
    public static final int REST_1_ID = START_SEQ + 2;
    public static final int REST_2_ID = START_SEQ + 3;

    public static final Restaurant REST_1 = new Restaurant(REST_1_ID, "LAGUNA");
    public static final Restaurant REST_2 = new Restaurant(REST_2_ID, "South Star");

/*    static {
        REST_1.setDishes(List.of(DISH_1, DISH_2));
    }*/

    public static Restaurant getNew() {
        return new Restaurant(null, "New Restaurant");
    }

    public static Restaurant getUpdated() {
        Restaurant updated = new Restaurant(REST_1);
        updated.setName("UpdatedName");
        return updated;
    }
}
