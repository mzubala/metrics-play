package metrics.play.domain;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ReservationService {

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final ApplicationEventPublisher publisher;

    public ReservationService(RoomRepository roomRepository, ReservationRepository reservationRepository, ApplicationEventPublisher publisher) {
        this.roomRepository = roomRepository;
        this.reservationRepository = reservationRepository;
        this.publisher = publisher;
    }

    @Transactional
    public void create(CreateReservationCommand command) {
        Room room = roomRepository.findByIdWithLock(command.getRoomId()).orElseThrow();
        if(reservationRepository.isReserved(command.getRoomId(), command.getFromInclusive(), command.getUntilExclusive())) {
            throw new RoomOccupiedException();
        }
        Reservation reservation = new Reservation(room, command.getFromInclusive(), command.getUntilExclusive());
        reservationRepository.save(reservation);
        publisher.publishEvent(new ReservationMade());
    }

}
