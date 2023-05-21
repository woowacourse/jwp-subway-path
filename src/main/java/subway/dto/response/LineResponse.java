package subway.dto.response;

import subway.domain.Line;
import subway.domain.Station;

import java.util.List;

public class LineResponse {
    private long id;
    private String name;
    private String color;
    private int extraFare;
    private List<StationResponse> stations;

    private LineResponse() {
    }

    private LineResponse(
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
        List<Station> orderedStations = line.getStationsUpwardToDownward();
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getExtraFare(),
                StationResponse.from(orderedStations)
        );
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

    public List<StationResponse> getStations() {
        return stations;
    }

    public int getExtraFare() {
        return extraFare;
    }
}
