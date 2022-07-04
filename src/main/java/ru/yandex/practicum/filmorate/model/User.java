package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@Slf4j
@Component
public class User {
    private Long id;
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
    private Set<Long> friends;


}

