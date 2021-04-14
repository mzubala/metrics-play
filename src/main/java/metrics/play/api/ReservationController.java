package metrics.play.api;

import io.micrometer.core.annotation.Timed;
import metrics.play.domain.CreateReservationCommand;
import metrics.play.domain.ReservationService;
import metrics.play.domain.RoomOccupiedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
@Timed
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public void create(@RequestBody CreateReservationCommand command) {
        reservationService.create(command);
    }

    @ExceptionHandler(RoomOccupiedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleRoomOccupied() {

    }
}
