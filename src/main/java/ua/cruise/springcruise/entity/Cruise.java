package ua.cruise.springcruise.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ua.cruise.springcruise.entity.dictionary.CruiseStatusDict;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
public class Cruise {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "liner_id")
    private Liner liner;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss")
    private LocalDateTime startDateTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm:ss")
    private LocalDateTime endDateTime;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private CruiseStatusDict status;

    @Column(length = 1024)
    private String description;

    private String imageName;

}
