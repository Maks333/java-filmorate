package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.BaseDbStorage;

import java.util.List;
import java.util.Optional;

@Repository("dbFilmStorage")
public class FilmDbStorage extends BaseDbStorage<Film> implements FilmStorage {
    private static final String FIND_BY_ID = "SELECT * FROM films WHERE id = ?";
    private static final String FIND_ALL_FILMS = "SELECT * FROM films";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, release_date, duration)" +
            "VALUES(?, ?, ?, ?) returning id";

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
                film.getRating());
        film.setId(id);
        if (!film.getGenres().isEmpty()) {
            for (Long genreId : film.getGenres()) {
                insert(INSERT_GENRES, film.getId(), genreId);
            }
        }

        return film;
    }

    @Override
    public Film update(Film film) {
        return null;
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

    @Override
    public void deleteById(long id) {

    }
}
