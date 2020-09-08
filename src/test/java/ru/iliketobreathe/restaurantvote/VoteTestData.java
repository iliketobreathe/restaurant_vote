package ru.iliketobreathe.restaurantvote;

import ru.iliketobreathe.restaurantvote.model.Vote;

import java.time.LocalDate;

import static ru.iliketobreathe.restaurantvote.RestaurantTestData.REST_1;
import static ru.iliketobreathe.restaurantvote.RestaurantTestData.REST_2;
import static ru.iliketobreathe.restaurantvote.UserTestData.ADMIN;
import static ru.iliketobreathe.restaurantvote.UserTestData.USER;
import static ru.iliketobreathe.restaurantvote.model.AbstractBaseEntity.START_SEQ;

public class VoteTestData {
    public static TestMatcher<Vote> VOTE_MATCHER = TestMatcher.usingEqualsAssertions(Vote.class);

    public static final int NOT_FOUND = 10;
    public static final int VOTE_1_ID = START_SEQ + 6;
    public static final int VOTE_2_ID = START_SEQ + 7;
    public static final int VOTE_3_ID = START_SEQ + 8;

    public static final Vote VOTE_1 = new Vote(100006, USER, REST_1, LocalDate.of(2020, 1, 30));
    public static final Vote VOTE_2 = new Vote(100007, ADMIN, REST_1, LocalDate.of(2020, 1, 31));
    public static final Vote VOTE_3 = new Vote(100008, ADMIN, REST_2, LocalDate.of(2020, 1, 28));

    public static Vote getNew() {
        return new Vote(null, USER, REST_1);
    }
}
