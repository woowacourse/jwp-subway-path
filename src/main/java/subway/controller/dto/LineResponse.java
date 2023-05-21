package subway.controller.dto;

import java.util.List;
import java.util.stream.Collectors;
import subway.domain.line.Line;
import subway.domain.station.Station;

public class LineResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final int extraFare;
    private final List<StationResponse> stations;

    public LineResponse(
            final Long id,
            final String name,
            final String color,
            final int extraFare,
            final List<StationResponse> stations
    ) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.stations = stations;
    }

    public static LineResponse from(final Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getExtraFare(),
                generateStations(line.getStations()));
    }

    private static List<StationResponse> generateStations(final List<Station> stations) {
        return stations.stream()
                .map(StationResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getExtraFare() {
        return extraFare;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
