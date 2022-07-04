package ua.cruise.springcruise.dto;


import lombok.*;
import ua.cruise.springcruise.entity.Cruise;
import ua.cruise.springcruise.entity.User;
import ua.cruise.springcruise.entity.dictionary.TicketStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TicketDTO {

    private Long id;

    @NotNull
    private Cruise cruise;

    @NotNull
    private User user;

    @NotEmpty
    private int position;

    @NotBlank
    private String imageName;

    @NotNull
    private BigDecimal price;

    private TicketStatus status;
}
