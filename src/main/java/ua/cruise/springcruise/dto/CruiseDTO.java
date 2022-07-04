package ua.cruise.springcruise.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.cruise.springcruise.entity.Liner;
import ua.cruise.springcruise.entity.Route;
import ua.cruise.springcruise.entity.dictionary.CruiseStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CruiseDTO {

    private Long id;

    @NotBlank
    @Size(min=2, max = 125)
    private String name;

    @NotNull
    private Liner liner;

    @NotNull
    private Route route;

    @NotNull
    private LocalDateTime startDateTime;

    @NotNull
    private LocalDateTime endDateTime;

    @NotNull
    private BigDecimal price;

    private CruiseStatus status;

    @NotBlank
    private String description;

    @NotBlank
    private String imageName;

    public void setStartDateTime(String startDateTime) throws DateTimeParseException {
        this.startDateTime = LocalDateTime.parse(startDateTime);
    }

    public void setEndDateTime(String endDateTime) throws DateTimeParseException{
        this.endDateTime = LocalDateTime.parse(endDateTime);
    }

    public void setPrice(String price) throws NumberFormatException{
        this.price = new BigDecimal(price);
    }

    public boolean isEndAfterStart(LocalDateTime startDateTime, LocalDateTime endDateTime){
        return endDateTime.isAfter(startDateTime);
    }
}
