package ua.cruise.springcruise.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LinerDTO {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Min(value = 1)
    private int passengerCapacity;

}
