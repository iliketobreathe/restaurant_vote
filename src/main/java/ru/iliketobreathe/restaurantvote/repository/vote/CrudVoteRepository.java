package ru.iliketobreathe.restaurantvote.repository.vote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.iliketobreathe.restaurantvote.model.Dish;
import ru.iliketobreathe.restaurantvote.model.Restaurant;
import ru.iliketobreathe.restaurantvote.model.Vote;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Transactional(readOnly = true)
public interface CrudVoteRepository extends JpaRepository<Vote, Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Vote v WHERE v.id=:id")
    int delete(@Param("id") int id);

    List<Vote> findByDate(LocalDate date);

    List<Vote> findByRestaurantIdAndDate(int restaurantId, LocalDate date);

    Vote findByUserIdAndDate(int userId, LocalDate date);
}
