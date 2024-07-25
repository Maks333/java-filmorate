package ru.yandex.practicum.filmorate.storage.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

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
    private static final String FIND_ALL_USERS_THAT_LIKE = "SELECT user_id FROM likes WHERE film_id = ?";
    private static final String FIND_ALL_FILM_GENRES = "SELECT genre_id FROM film_to_genre WHERE film_id = ?";

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

        List<Long> genres = jdbc.queryForList(FIND_ALL_FILM_GENRES, Long.class, film.getId());
        film.setGenres(new HashSet<>(genres));

        film.setRating(rs.getLong("rating_id"));
        return film;
    }
}
