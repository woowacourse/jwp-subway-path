package subway.dto;

import subway.domain.Station;

import java.util.List;

public class PathDto {

    private final List<Station> stations;
    private final double distance;

    public PathDto(final List<Station> stations, final double distance) {
        this.stations = stations;
        this.distance = distance;
    }

    public List<Station> stations() {
        return stations;
    }

    public double distance() {
        return distance;
    }
}
