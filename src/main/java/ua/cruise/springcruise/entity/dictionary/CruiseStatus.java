package ua.cruise.springcruise.entity.dictionary;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Entity(name="cruise_status")
public class CruiseStatus {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
}
