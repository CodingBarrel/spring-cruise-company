package ua.cruise.springcruise.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "cruise_id")
    @ToString.Exclude
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
