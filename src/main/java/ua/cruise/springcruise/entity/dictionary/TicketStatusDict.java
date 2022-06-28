package ua.cruise.springcruise.entity.dictionary;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity(name="ticket_status_dict")
public class TicketStatusDict {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
}
