package ru.yandex.practicum.filmorate.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Genre {
    @JsonProperty("name")
    @NotBlank String title;
    private Integer id;

    @JsonCreator
    public static Genre forObject(@JsonProperty("id") int id, @JsonProperty String title) {
        return new Genre(title, id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return getId().equals(genre.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
