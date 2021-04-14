package metrics.play.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import metrics.play.domain.RoomDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public class RoomsPage extends PageImpl<RoomDto> {

    private static final long serialVersionUID = 3248189030448292002L;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RoomsPage(@JsonProperty("content") List<RoomDto> content, @JsonProperty("number") int number, @JsonProperty("size") int size,
                     @JsonProperty("totalElements") Long totalElements, @JsonProperty("pageable") JsonNode pageable, @JsonProperty("last") boolean last,
                     @JsonProperty("totalPages") int totalPages, @JsonProperty("sort") JsonNode sort, @JsonProperty("first") boolean first,
                     @JsonProperty("numberOfElements") int numberOfElements) {
        super(content, PageRequest.of(number, size), totalElements);
    }

    public RoomsPage(List<RoomDto> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public RoomsPage(List<RoomDto> content) {
        super(content);
    }

    public RoomsPage() {
        super(new ArrayList<>());
    }

}
