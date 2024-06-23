package ru.yandex.practicum.filmorate.model;

import com.google.gson.annotations.JsonAdapter;
import lombok.Data;
import ru.yandex.practicum.filmorate.adapter.DurationAdapter;
import ru.yandex.practicum.filmorate.adapter.LocalDateAdapter;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

@Data
public class Film {
    private long id;
    private String name;
    private String description;
    @JsonAdapter(LocalDateAdapter.class)
    private LocalDate releaseDate;
    @JsonAdapter(DurationAdapter.class)
    private Duration duration;
    private Set<Long> likes;
}