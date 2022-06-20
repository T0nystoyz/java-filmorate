package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.utils.IsAfter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
@Slf4j
@Data
public class Film {
    @Positive
    private long id = IdGenerator.nextId();
    @NotBlank
    private String name;
    @Size(max = 200, message = "допустимый размер описания: 200 символов")
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @IsAfter(current = "1895-12-28", message = "до 28 декабря 1895 года фильмов не существовало")
    private LocalDate releaseDate;
    @Min(0)
    private int duration;

    private static class IdGenerator {
        private static long id = 0;

        public static long nextId() {
            return ++id;
        }
    }
}
