package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Value
@Builder
@AllArgsConstructor
public class MPARating {
        Integer id;

        @JsonProperty("name")
        @NotBlank String title;

}

