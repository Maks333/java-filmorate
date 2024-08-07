package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.List;
import java.util.Optional;

@Repository("dbFilmStorage")
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    private static final String FIND_BY_ID = "SELECT * FROM films WHERE id = ?";
    private static final String FIND_ALL_FILMS = "SELECT * FROM films";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, release_date, duration, rating_id) " +
            "VALUES(?, ?, ?, ?, ?)";
    private static final String INSERT_GENRES = "INSERT INTO film_to_genre(film_id, genre_id)" +
            "VALUES(?, ?)";

    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?, duration = ?, rating_id = ? WHERE id = ?";
    private static final String DELETE_GENRES = "DELETE FROM film_to_genre WHERE film_id = ?";

    private static final String ADD_LIKE = "INSERT INTO likes(user_id, film_id) VALUES(?, ?)";
    private static final String REMOVE_LIKE = "DELETE FROM likes WHERE user_id = ? AND film_id = ?";

    private static final String GET_BY_LIKES = "SELECT f.id, name, description, release_date, duration, rating_id, COUNT(*) "
            + " FROM films AS f"
            + " JOIN likes AS l ON f.id = l.film_id"
            + " GROUP BY f.id"
            + " ORDER BY COUNT(*) desc"
            + " LIMIT ?";


    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Film> getFilms() {
        return findMany(FIND_ALL_FILMS);
    }

    @Override
    public Film create(Film film) {
        long id = insert(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId());
        film.setId(id);
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                insert(INSERT_GENRES, film.getId(), genre.getId());
            }
        }

        return film;
    }

    @Override
    public Film update(Film film) {
        update(UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration().toMinutes(),
                film.getMpa().getId(),
                film.getId());

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            delete(DELETE_GENRES, film.getId());
            for (Genre genre : film.getGenres()) {
                insert(INSERT_GENRES, film.getId(), genre.getId());
            }
        }
        return getFilmById(film.getId());
    }

    @Override
    public void likeFilm(long id, long userId) {
        insert(ADD_LIKE, userId, id);
    }

    @Override
    public void unlikeFilm(long id, long userId) {
        jdbc.update(REMOVE_LIKE, userId, id);
    }

    @Override
    public List<Film> getFilmsByLikes(long count) {
        return findMany(GET_BY_LIKES, count);
    }

    @Override
    public Film getFilmById(long id) {
        Optional<Film> film = findOne(FIND_BY_ID, id);
        if (film.isPresent()) {
            return film.get();
        } else {
            throw new NotFoundException("Film with id = " + id + " is not found");
        }
    }
}
