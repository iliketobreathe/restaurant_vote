package ru.iliketobreathe.restaurantvote.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.iliketobreathe.restaurantvote.DishTestData;
import ru.iliketobreathe.restaurantvote.model.Dish;
import ru.iliketobreathe.restaurantvote.util.exception.NotFoundException;
import ru.iliketobreathe.restaurantvote.web.json.JsonUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.iliketobreathe.restaurantvote.DishTestData.*;
import static ru.iliketobreathe.restaurantvote.RestaurantTestData.REST_1_ID;
import static ru.iliketobreathe.restaurantvote.TestUtil.readFromJson;
import static ru.iliketobreathe.restaurantvote.TestUtil.userHttpBasic;
import static ru.iliketobreathe.restaurantvote.UserTestData.ADMIN;

//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminDishRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = AdminDishRestController.REST_URL + "/";

    @Autowired
    private AdminDishRestController controller;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + REST_1_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(DISH_1, DISH_2));
    }

    @Test
    void get() throws Exception{
        perform(MockMvcRequestBuilders.get(REST_URL + DISH_1_ID + "/restaurants/" + REST_1_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(DISH_MATCHER.contentJson(DISH_1));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    @Sql(scripts = "classpath:db/populateDB.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void delete() throws Exception{
        perform(MockMvcRequestBuilders.delete(REST_URL + DISH_1_ID + "/restaurants/" + REST_1_ID)
                .with(userHttpBasic(ADMIN)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> controller.get(DISH_1_ID, REST_1_ID));
        DISH_MATCHER.assertMatch(List.of(DISH_2), controller.getAll(REST_1_ID));
    }

    @Test
    void createWithLocation() throws Exception {
        Dish newDish = DishTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + REST_1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newDish))
                .with(userHttpBasic(ADMIN)))
                .andDo(print());

        Dish created = readFromJson(action, Dish.class);
        int newId = created.id();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(controller.get(newId, REST_1_ID), newDish);
    }

    @Test
    void update() throws Exception{
        Dish updated = DishTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + DISH_1_ID + "/restaurants/" + REST_1_ID).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isNoContent());

        DISH_MATCHER.assertMatch(controller.get(DISH_1_ID, REST_1_ID), updated);
    }
}