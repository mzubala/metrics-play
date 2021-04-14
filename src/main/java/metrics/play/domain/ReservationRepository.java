package metrics.play.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ReservationRepository extends CrudRepository<Reservation, Long> {

    @Query("SELECT (count(r) > 0) " +
            "FROM Reservation r " +
            "WHERE r.room.id = :roomId AND ((:start <= r.fromInclusive AND :end > r.fromInclusive) " +
            "OR (:start >= r.fromInclusive AND :end <= r.untilExclusive) " +
            "OR (:start < r.untilExclusive AND :end >= r.untilExclusive))"
    )
    Boolean isReserved(@Param("roomId") Long roomId, @Param("start") LocalDate start, @Param("end") LocalDate end);

    Long countByUntilExclusiveLessThanEqual(LocalDate date);

    Long countByFromInclusiveGreaterThan(LocalDate date);
}
