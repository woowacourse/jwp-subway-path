package subway.dto;

import java.util.List;
import java.util.Objects;
import subway.domain.Distance;
import subway.domain.Fare;

public class ShortPathResponse {

    private final List<String> stations;
    private final Distance distance;
    private final Fare fare;

    public ShortPathResponse(List<String> stations, Distance distance, Fare fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<String> getStations() {
        return stations;
    }

    public Distance getDistance() {
        return distance;
    }

    public Fare getFare() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ShortPathResponse that = (ShortPathResponse) o;
        return Objects.equals(stations, that.stations) && Objects.equals(distance, that.distance)
                && Objects.equals(fare, that.fare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stations, distance, fare);
    }
    
}
