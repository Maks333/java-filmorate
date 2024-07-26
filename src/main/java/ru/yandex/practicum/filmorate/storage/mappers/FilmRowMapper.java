package ru.yandex.practicum.filmorate.storage.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Component
public class FilmRowMapper implements RowMapper<Film> {
    private final JdbcTemplate jdbc;
    private final RowMapper<Genre> mapper;
    private final RowMapper<Mpa> mapper1;

    private static final String FIND_ALL_USERS_THAT_LIKE = "SELECT user_id FROM likes WHERE film_id = ?";
    private static final String FIND_ALL_FILM_GENRES = "SELECT g.id, g.name FROM film_to_genre AS ftg " +
            " JOIN genres AS g ON g.id = ftg.genre_id" +
            " WHERE ftg.film_id = ?";
    private static final String FIND_FILM_RATING = "SELECT r.id, r.name FROM films AS f" +
            " JOIN ratings AS r ON r.id = f.rating_id" +
            " WHERE f.id = ?";

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));

        Date releaseDate = rs.getDate("release_date");
        film.setReleaseDate(releaseDate.toLocalDate());

        film.setDuration(Duration.ofMinutes(rs.getLong("duration")));

        List<Long> likes = jdbc.queryForList(FIND_ALL_USERS_THAT_LIKE, Long.class, film.getId());
        film.setLikes(new HashSet<>(likes));

        List<Genre> genres = jdbc.query(FIND_ALL_FILM_GENRES, mapper, film.getId());
        film.setGenres(new HashSet<>(genres));

        film.setMpa(jdbc.queryForObject(FIND_FILM_RATING, mapper1, film.getId()));
        return film;
    }
}
