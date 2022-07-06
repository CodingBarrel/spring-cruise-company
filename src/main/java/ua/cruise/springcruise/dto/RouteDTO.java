package ua.cruise.springcruise.dto;

import lombok.*;
import ua.cruise.springcruise.entity.RoutePoint;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RouteDTO {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private List<RoutePoint> routePointList;
}
