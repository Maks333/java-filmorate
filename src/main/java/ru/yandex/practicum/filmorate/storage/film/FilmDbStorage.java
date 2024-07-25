package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Repository("dbFilmStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private static final String FIND_BY_ID = "SELECT * FROM films WHERE id = ?";

    private final JdbcTemplate jdbc;
    private final RowMapper<Film> mapper;

    @Override
    public List<Film> getFilms() {
        return List.of();
    }

    @Override
    public Film create(Film film) {
        return null;
    }

    @Override
    public Film update(Film film) {
        return null;
    }

    @Override
    public Film getFilmById(long id) {
        return jdbc.queryForObject(FIND_BY_ID, mapper, id);
    }

    @Override
    public void deleteById(long id) {

    }
}
