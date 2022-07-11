package ua.cruise.springcruise.dto;

import lombok.*;
import ua.cruise.springcruise.validation.NameMatch;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LinerDTO {

    private Long id;

    @NameMatch
    private String name;

    @NotNull
    @PositiveOrZero
    private int passengerCapacity;

}
