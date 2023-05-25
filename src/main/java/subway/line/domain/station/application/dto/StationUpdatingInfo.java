package subway.line.domain.station.application.dto;

public class StationUpdatingInfo {
    private final String name;

    public StationUpdatingInfo(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
