package ru.yandex.practicum.filmorate.model;

import com.google.gson.annotations.JsonAdapter;
import lombok.Data;
import ru.yandex.practicum.filmorate.adapter.LocalDateAdapter;

import java.time.LocalDate;

@Data
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    @JsonAdapter(LocalDateAdapter.class)
    private LocalDate birthday;

}
