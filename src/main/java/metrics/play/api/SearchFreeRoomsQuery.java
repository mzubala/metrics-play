package metrics.play.api;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class SearchFreeRoomsQuery {
    @NotNull
    private String city;
    @NotNull
    private LocalDate fromInclusive;
    @NotNull
    private LocalDate endExclusive;
    @NotNull
    private Integer pageNumber;

    public SearchFreeRoomsQuery() {
    }

    public SearchFreeRoomsQuery(@NotNull String city, @NotNull LocalDate fromInclusive, @NotNull LocalDate endExclusive, @NotNull Integer pageNumber) {
        this.city = city;
        this.fromInclusive = fromInclusive;
        this.endExclusive = endExclusive;
        this.pageNumber = pageNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDate getFromInclusive() {
        return fromInclusive;
    }

    public void setFromInclusive(LocalDate fromInclusive) {
        this.fromInclusive = fromInclusive;
    }

    public LocalDate getEndExclusive() {
        return endExclusive;
    }

    public void setEndExclusive(LocalDate endExclusive) {
        this.endExclusive = endExclusive;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public SearchFreeRoomsQuery withPageNumber(Integer pageNumber) {
        return new SearchFreeRoomsQuery(city, fromInclusive, endExclusive, pageNumber);
    }
}
