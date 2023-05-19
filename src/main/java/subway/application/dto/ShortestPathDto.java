package subway.application.dto;

import java.util.List;
import subway.domain.Path;
import subway.domain.Station;

public class ShortestPathDto {
    private List<Station> stations;
    private double distance;
    private int fare;

    public ShortestPathDto(Path path, int fare) {
        this.stations = path.getStations();
        this.distance = path.getDistance();
        this.fare = fare;
    }

    public List<Station> getStations() {
        return stations;
    }

    public double getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
