package ua.cruise.springcruise.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.cruise.springcruise.entity.dictionary.UserRoleDict;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class User {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private String login;

    private String password;

    @ManyToOne
    @JoinColumn(name = "user_role_id")
    private UserRoleDict role;

    private LocalDateTime lastLoginDate;
}
