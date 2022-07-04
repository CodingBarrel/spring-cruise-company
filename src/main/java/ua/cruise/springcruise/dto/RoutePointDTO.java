package ua.cruise.springcruise.dto;

import lombok.*;
import ua.cruise.springcruise.entity.Route;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RoutePointDTO {

    private Long id;

    @NotNull
    private Route route;

    @NotBlank
    private String name;
}
