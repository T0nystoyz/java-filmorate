package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.utils.IsAfter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Slf4j
@Data
@Component
public class Film {
/*    @Nullable
    @Positive*/
    private Long id;
    @NotBlank
    private String name;
    @Size(max = 200, message = "допустимый размер описания: 200 символов")
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @IsAfter(current = "1895-12-28", message = "до 28 декабря 1895 года фильмов не существовало")
    private LocalDate releaseDate;
    @Min(0)
    private int duration;
    private Integer rate;


}
