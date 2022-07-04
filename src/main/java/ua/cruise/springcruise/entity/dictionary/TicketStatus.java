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
@Entity(name="ticket_status")
public class TicketStatus {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
}
