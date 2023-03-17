package metrics.play.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT r FROM Room r WHERE r.id = ?1")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Room> findByIdWithLock(Long id);

    @Query("SELECT new metrics.play.domain.RoomDto(room.id, room.name, room.city) " +
            "FROM Room room " +
            "LEFT JOIN room.reservations r ON ((:start <= r.fromInclusive AND :end > r.fromInclusive) OR (:start >= r.fromInclusive AND :end <= r.untilExclusive) OR (:start < r.untilExclusive AND :end >= r.untilExclusive)) " +
            "WHERE (room.city = :city) AND (r.id IS NULL)"
    )
    Page<RoomDto> searchFreeRooms(@Param("city") String city, @Param("start") LocalDate start, @Param("end") LocalDate end, Pageable pageable);
}
