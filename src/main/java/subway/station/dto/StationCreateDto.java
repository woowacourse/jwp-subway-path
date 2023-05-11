package subway.station.dto;

import java.beans.ConstructorProperties;

public class StationCreateDto {

    private final String name;

    @ConstructorProperties(value = "name")
    public StationCreateDto(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
