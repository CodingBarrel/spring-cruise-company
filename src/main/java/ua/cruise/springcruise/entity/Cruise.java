package ua.cruise.springcruise.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ua.cruise.springcruise.entity.dictionary.CruiseStatusDict;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Cruise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "liner_id")
    private Liner liner;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private CruiseStatusDict status;

    private String description;

    private String imageName;

}
