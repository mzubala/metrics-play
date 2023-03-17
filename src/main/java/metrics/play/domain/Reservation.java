package metrics.play.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@SequenceGenerator(name = "reservation_seq")
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservation_seq")
    private Long id;

    @ManyToOne
    private Room room;

    private LocalDate fromInclusive;
    private LocalDate untilExclusive;

    public Reservation(Room room, LocalDate fromInclusive, LocalDate untilExclusive) {
        this.room = room;
        this.fromInclusive = fromInclusive;
        this.untilExclusive = untilExclusive;
    }

    protected Reservation() {}
}
