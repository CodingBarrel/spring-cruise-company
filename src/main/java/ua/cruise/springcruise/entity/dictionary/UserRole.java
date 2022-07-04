package ua.cruise.springcruise.entity.dictionary;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity(name="user_role")
public class UserRole implements GrantedAuthority {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
