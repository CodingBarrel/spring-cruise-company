package ua.cruise.springcruise.dto;

import lombok.*;
import ua.cruise.springcruise.entity.dictionary.UserRole;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDTO {

    private Long id;

    @NotBlank
    private String login;

    @NotBlank
    private String password;

    private UserRole role;

    private LocalDateTime lastLoginDateTime;
}
