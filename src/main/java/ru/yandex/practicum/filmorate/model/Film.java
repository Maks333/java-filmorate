package ru.yandex.practicum.filmorate.model;

import com.google.gson.annotations.JsonAdapter;
import lombok.Data;
import ru.yandex.practicum.filmorate.adapter.DurationAdapter;
import ru.yandex.practicum.filmorate.adapter.LocalDateAdapter;

import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    private long id;
    private String name;
    private String description;
    @JsonAdapter(LocalDateAdapter.class)
    private LocalDate releaseDate;
    @JsonAdapter(DurationAdapter.class)
    private Duration duration;
}