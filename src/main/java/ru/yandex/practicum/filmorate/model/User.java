package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Slf4j
public class User {
    @Positive
    public long id = IdGenerator.nextId();
    @NotBlank(message = "поле *login* не может быть пустым")
    @Pattern(regexp = "^[A-Za-z\\d]*$", message = "поле *login* не должно содержать пробелы и спец. символы")
    private String login;
    private String name;
    @NotBlank(message = "поле *email* не может быть пустым")
    @Email(message = "неверный формат поля *email*")
    private String email;
    @Past(message = "поле *birthday* не может указывать на будущую дату")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    private static class IdGenerator {
        private static long id = 0;

        public static long nextId() {
            return ++id;
        }
    }
}

