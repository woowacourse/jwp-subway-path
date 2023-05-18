package subway.application.route.port.in.find;

import java.util.List;

public class RouteFindResponseDto {

    private final List<Long> stations;
    private final int distance;
    private final int fare;

    public RouteFindResponseDto(final List<Long> stations, final int distance, final int fare) {
        this.stations = stations;
        this.distance = distance;
        this.fare = fare;
    }

    public List<Long> getStations() {
        return stations;
    }

    public int getDistance() {
        return distance;
    }

    public int getFare() {
        return fare;
    }
}
