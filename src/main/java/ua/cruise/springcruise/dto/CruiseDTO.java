package ua.cruise.springcruise.dto;

import lombok.*;
import ua.cruise.springcruise.entity.Liner;
import ua.cruise.springcruise.entity.Route;
import ua.cruise.springcruise.entity.dictionary.CruiseStatus;
import ua.cruise.springcruise.validation.DateTimeMatch;
import ua.cruise.springcruise.validation.NameMatch;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@DateTimeMatch.List({
        @DateTimeMatch(
                firstDateTime = "startDateTime",
                secondDateTime = "endDateTime")})

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CruiseDTO {

    private Long id;

    @NameMatch
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
    @PositiveOrZero
    private BigDecimal price;

    private CruiseStatus status;

    @NotBlank
    private String description;

    private String imageName;

}
