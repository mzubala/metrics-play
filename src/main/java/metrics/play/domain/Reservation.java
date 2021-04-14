package metrics.play.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
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
