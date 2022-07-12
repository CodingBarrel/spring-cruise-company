package ua.cruise.springcruise.dto;

import lombok.*;
import ua.cruise.springcruise.entity.RoutePoint;
import ua.cruise.springcruise.validation.NameMatch;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RouteDTO {

    private Long id;

    @NameMatch
    private String name;

    private List<RoutePoint> routePointList;
}
