package subway.application.dto.path;

import java.math.BigDecimal;
import java.util.List;
import subway.domain.Distance;
import subway.domain.Fare;
import subway.domain.Path;
import subway.domain.Station;

public class ShortestPathDto {
    private List<Station> stations;
    private Distance distance;
    private Fare fare;

    public ShortestPathDto(Path path, Fare fare) {
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

    public BigDecimal getFare() {
        return fare.price();
    }
}
