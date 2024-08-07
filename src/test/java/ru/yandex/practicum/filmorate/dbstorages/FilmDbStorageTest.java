package ru.yandex.practicum.filmorate.dbstorages;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
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
@Sql(value = {"/schema.sql", "/testing.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
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
        Mpa mpa = new Mpa();
        mpa.setId(2L);
        film.setMpa(mpa);
        Genre genre1 = new Genre();
        genre1.setId(1L);
        Genre genre2 = new Genre();
        genre2.setId(2L);
        Genre genre3 = new Genre();
        genre3.setId(3L);

        film.setGenres(new HashSet<>(List.of(genre1, genre2, genre3)));

        film = filmStorage.create(film);
        assertThat(film)
                .hasFieldOrPropertyWithValue("id", 6L);

        Film filmFromDb = filmStorage.getFilmById(film.getId());
        assertThat(filmFromDb)
                .hasFieldOrPropertyWithValue("id", 6L);
    }

    @Test
    public void testFilmUpdate() {
        Film film = new Film();
        film.setName("Film10");
        film.setDescription("Film10Desc");
        film.setDuration(Duration.ofMinutes(30));
        film.setReleaseDate(LocalDate.of(1999, 5, 10));
        Mpa mpa = new Mpa();
        mpa.setId(2L);
        film.setMpa(mpa);
        Genre genre1 = new Genre();
        genre1.setId(1L);
        film.setGenres(new HashSet<>(List.of(genre1)));
        film.setId(1);
        Film updatedFilm = filmStorage.update(film);
        assertThat(updatedFilm)
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    public void testAddLike() {
        filmStorage.likeFilm(1, 5);

        Film film = filmStorage.getFilmById(1);
        List<Long> likes = film.getLikes().stream().toList();
        assertThat(likes).size().isEqualTo(5);
        assertThat(likes.getLast()).isEqualTo(5);
    }

    @Test
    public void testsRemoveLike() {
        filmStorage.unlikeFilm(1, 1);

        Film film = filmStorage.getFilmById(1);
        List<Long> likes = film.getLikes().stream().toList();
        assertThat(likes).size().isEqualTo(3);
        assertThat(likes).doesNotContain(1L);
    }

    @Test
    public void testGetByLikes() {
        List<Film> filmsByLikes = filmStorage.getFilmsByLikes(3);

        assertThat(filmsByLikes.getFirst().getLikes()).size().isEqualTo(4);
        assertThat(filmsByLikes.get(1).getLikes()).size().isEqualTo(4);
        assertThat(filmsByLikes.getLast().getLikes()).size().isEqualTo(2);
        System.out.println(filmsByLikes);
    }
}
