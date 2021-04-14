package metrics.play.domain;

public class RoomDto {
    private Long id;
    private String name;
    private String city;

    public RoomDto(Long id, String name, String city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }

    public RoomDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
