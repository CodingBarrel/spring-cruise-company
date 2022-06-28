package ua.cruise.springcruise.entity;

import lombok.*;
import ua.cruise.springcruise.entity.dictionary.TicketStatusDict;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cruise_id")
    private Cruise cruise;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int position;

    private String docsImageName;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "status_id")
    private TicketStatusDict status;
    
}
