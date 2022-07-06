package ua.cruise.springcruise.dto;

import lombok.*;
import ua.cruise.springcruise.entity.Liner;
import ua.cruise.springcruise.entity.Route;
import ua.cruise.springcruise.entity.dictionary.CruiseStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CruiseDTO {

    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private Liner liner;

    @NotNull
    private Route route;

    @NotBlank
    private String startDateTime;

    @NotBlank
    private String endDateTime;

    @NotNull
    private BigDecimal price;

    private CruiseStatus status;

    @NotBlank
    private String description;

    private String imageName;

}
