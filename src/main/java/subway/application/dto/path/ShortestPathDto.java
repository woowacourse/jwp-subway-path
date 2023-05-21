package subway.application.dto.path;

import java.util.List;
import subway.domain.Distance;
import subway.domain.Path;
import subway.domain.Station;

public class ShortestPathDto {
    private List<Station> stations;
    private Distance distance;
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
        return distance.value();
    }

    public int getFare() {
        return fare;
    }
}
