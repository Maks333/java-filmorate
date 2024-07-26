package ru.yandex.practicum.filmorate.dbstorages;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@AutoConfigureTestDatabase
@JdbcTest
@ComponentScan("ru.yandex.practicum.filmorate")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final FilmDbStorage filmStorage;

    @Test
    public void testFindFilmById() {
        Film film = filmStorage.getFilmById(1);

        assertThat(film)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("likes", new HashSet<>(List.of(1L, 2L, 3L, 4L)));
    }

    @Test
    public void testFindAllFilms() {
        List<Film> films = filmStorage.getFilms();


        assertThat(films)
                .size().isEqualTo(5);
    }

    @Test
    public void testFilmCreation() {
        Film film = new Film();
        film.setName("Film10");
        film.setDescription("Film10Desc");
        film.setDuration(Duration.ofMinutes(30));
        film.setReleaseDate(LocalDate.of(1999, 5, 10));
        film.setRating(2L);
        film.setGenres(new HashSet<>(List.of(1L, 2L, 3L)));

        film = filmStorage.create(film);
        assertThat(film)
                .hasFieldOrPropertyWithValue("id", 6L);

        Film filmFromDb = filmStorage.getFilmById(film.getId());
        assertThat(filmFromDb)
                .hasFieldOrPropertyWithValue("id", 6L);
        System.out.println(filmFromDb);
        System.out.println(filmFromDb.getDuration().toMinutes());
    }

    @Test
    public void testFilmUpdate() {
        Film filmFromDb = filmStorage.getFilmById(1);
        System.out.println(filmFromDb);

        Film film = new Film();
        film.setName("Film10");
        film.setDescription("Film10Desc");
        film.setDuration(Duration.ofMinutes(30));
        film.setReleaseDate(LocalDate.of(1999, 5, 10));
        film.setRating(2L);
        film.setGenres(new HashSet<>(List.of(1L)));
        film.setId(1);
        Film updatedFilm = filmStorage.update(film);

        System.out.println(updatedFilm);
    }
}
