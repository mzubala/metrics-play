package metrics.play.api;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import metrics.play.domain.CreateRoomCommand;
import metrics.play.domain.Room;
import metrics.play.domain.RoomDto;
import metrics.play.domain.RoomRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

@Timed
@RestController
@RequestMapping("/rooms")
public class RoomController implements InitializingBean {

    private static final int PAGE_SIZE = 50;
    private final RoomRepository roomRepository;
    private final MeterRegistry meterRegistry;
    private DistributionSummary fileSizesSummary;

    public RoomController(RoomRepository roomRepository, MeterRegistry meterRegistry) {
        this.roomRepository = roomRepository;
        this.meterRegistry = meterRegistry;
    }

    @PostMapping
    public void createRoom(@RequestBody @Valid CreateRoomCommand command) {
        Room room = new Room(command.getName(), command.getCity());
        roomRepository.save(room);
        this.fileSizesSummary.record(Math.random() * 10_000_000.0);
    }

    @GetMapping
    public Page<RoomDto> searchFreeRooms(SearchFreeRoomsQuery query) {
        return roomRepository.searchFreeRooms(query.getCity(), query.getFromInclusive(), query.getEndExclusive(), PageRequest.of(query.getPageNumber(), PAGE_SIZE));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.fileSizesSummary = this.meterRegistry.summary("roomPics", List.of(Tag.of("name", "roomPics")));
    }
}
