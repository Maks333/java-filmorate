package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MpaDbStorage {
    private final JdbcTemplate jdbc;
    private final RowMapper<Mpa> mapper;

    private static final String FIND_FILM_RATING = "SELECT * FROM ratings";
    private static final String FIND_FILM_RATING_BY_ID = "SELECT * FROM ratings WHERE id = ?";

    public List<Mpa> getAllMpa() {
        return jdbc.query(FIND_FILM_RATING, mapper);
    }

    public Mpa getMpaById(long id) {
        try {
            Mpa mpa = jdbc.queryForObject(FIND_FILM_RATING_BY_ID, mapper, id);
            return mpa;
        } catch (RuntimeException ignored) {
            throw new NotFoundException("Mpa is not found");
        }
    }
}
