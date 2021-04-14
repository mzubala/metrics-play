package metrics.play.domain;

import javax.validation.constraints.NotNull;

public class CreateRoomCommand {
    @NotNull
    private String name;
    @NotNull
    private String city;

    public CreateRoomCommand(@NotNull String name, @NotNull String city) {
        this.name = name;
        this.city = city;
    }

    public CreateRoomCommand() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
