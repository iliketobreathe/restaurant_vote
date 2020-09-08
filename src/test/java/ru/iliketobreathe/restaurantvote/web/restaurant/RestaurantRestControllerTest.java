package ru.iliketobreathe.restaurantvote.web.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.iliketobreathe.restaurantvote.VoteTestData;
import ru.iliketobreathe.restaurantvote.model.Restaurant;
import ru.iliketobreathe.restaurantvote.model.Vote;
import ru.iliketobreathe.restaurantvote.repository.vote.DataJpaVoteRepository;
import ru.iliketobreathe.restaurantvote.web.AbstractControllerTest;
import ru.iliketobreathe.restaurantvote.web.json.JsonUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.iliketobreathe.restaurantvote.RestaurantTestData.*;
import static ru.iliketobreathe.restaurantvote.TestUtil.readFromJson;
import static ru.iliketobreathe.restaurantvote.TestUtil.userHttpBasic;
import static ru.iliketobreathe.restaurantvote.UserTestData.ADMIN;
import static ru.iliketobreathe.restaurantvote.UserTestData.USER;
import static ru.iliketobreathe.restaurantvote.VoteTestData.VOTE_MATCHER;

class RestaurantRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = RestaurantRestController.REST_URL + "/";

    @Autowired
    RestaurantRestController controller;

    @Autowired
    DataJpaVoteRepository voteRepository;

    @Test
    void getAll() throws Exception{
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(List.of(REST_1)));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + REST_1_ID)
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(REST_1));
    }

    @Test
    void vote() throws Exception {
        Vote newVote = VoteTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.get(REST_URL + REST_1_ID + "/vote")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newVote))
                .with(userHttpBasic(USER)));

        Vote created = readFromJson(action, Vote.class);
        int newId = created.id();
        newVote.setId(newId);
        VOTE_MATCHER.assertMatch(created, newVote);
        VOTE_MATCHER.assertMatch(voteRepository.get(newId), newVote);
    }
}