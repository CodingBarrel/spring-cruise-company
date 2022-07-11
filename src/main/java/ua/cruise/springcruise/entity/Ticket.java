package ua.cruise.springcruise.entity;

import lombok.*;
import ua.cruise.springcruise.entity.dictionary.TicketStatus;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cruise_id")
    private Cruise cruise;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int position;

    private String imageName;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private TicketStatus status;

}
