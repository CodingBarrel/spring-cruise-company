package ua.cruise.springcruise.dto;

import lombok.*;
import ua.cruise.springcruise.entity.dictionary.UserRole;
import ua.cruise.springcruise.validation.LoginMatch;
import ua.cruise.springcruise.validation.PasswordMatch;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDTO {

    private Long id;

    @LoginMatch
    private String login;

    @PasswordMatch
    private String password;

    private UserRole role;

    private LocalDateTime lastLoginDateTime;
}
