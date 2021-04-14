package metrics.play.domain;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class CreateReservationCommand {
    @NotNull
    private Long roomId;
    @NotNull
    private LocalDate fromInclusive;
    @NotNull
    private LocalDate untilExclusive;

    public CreateReservationCommand() {
    }

    public CreateReservationCommand(@NotNull Long roomId, @NotNull LocalDate fromInclusive, @NotNull LocalDate untilExclusive) {
        this.roomId = roomId;
        this.fromInclusive = fromInclusive;
        this.untilExclusive = untilExclusive;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public LocalDate getFromInclusive() {
        return fromInclusive;
    }

    public void setFromInclusive(LocalDate fromInclusive) {
        this.fromInclusive = fromInclusive;
    }

    public LocalDate getUntilExclusive() {
        return untilExclusive;
    }

    public void setUntilExclusive(LocalDate untilExclusive) {
        this.untilExclusive = untilExclusive;
    }
}
