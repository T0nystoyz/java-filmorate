package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.springframework.stereotype.Component;


@Value
@Data
@Builder
public class Friendship {
    User user;
    User friend;
}
