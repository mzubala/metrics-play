package metrics.play.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.List;

@Entity
@SequenceGenerator(name = "room_seq")
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "room_seq")
    private Long id;

    private String name;
    private String city;

    @OneToMany(mappedBy = "room")
    private List<Reservation> reservations;

    public Room(String name, String city) {
        this.name = name;
        this.city = city;
    }

    Room() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }
}
