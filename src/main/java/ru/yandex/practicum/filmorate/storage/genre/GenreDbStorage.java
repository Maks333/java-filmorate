package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage {
    private final JdbcTemplate jdbc;
    private final RowMapper<Genre> mapper;

    private static final String FIND_FILM_GENRES = "SELECT * FROM genres ORDER BY id";
    private static final String FIND_FILM_GENRE_BY_ID = "SELECT * FROM genres WHERE id = ?";

    public List<Genre> getAllGenres() {
        return jdbc.query(FIND_FILM_GENRES, mapper);
    }

    public Genre getGenreById(long id) {
        try {
            return jdbc.queryForObject(FIND_FILM_GENRE_BY_ID, mapper, id);
        } catch (RuntimeException ignored) {
            throw new NotFoundException("Genre is not found");
        }
    }
}
