package ru.yandex.practicum.filmorate.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

public class DurationAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter out, Duration value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.toMinutes());
        }
    }

    @Override
    public Duration read(JsonReader in) throws IOException {
        try {
            return Duration.ofMinutes(Long.parseLong(in.nextString()));
        } catch (NumberFormatException exception) {
            return null;
        }
    }
}