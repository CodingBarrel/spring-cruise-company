package ua.cruise.springcruise.dto;

import lombok.*;
import ua.cruise.springcruise.entity.Route;
import ua.cruise.springcruise.validation.NameMatch;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RoutePointDTO {

    private Long id;

    private Route route;

    @NameMatch
    private String name;
}
