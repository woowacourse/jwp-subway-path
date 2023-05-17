package subway.dto.response;

import subway.domain.Line;
import subway.domain.Station;

import java.util.List;

public class LineResponse {
    private Long id;
    private String name;
    private String color;
    private List<StationResponse> stations;
    private int extraFare;

    private LineResponse() {
    }

    private LineResponse(
            final Long id,
            final String name,
            final String color,
            final List<StationResponse> stations,
            final int extraFare
    ) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stations = stations;
        this.extraFare = extraFare;
    }

    public static LineResponse from(final Line line) {
        List<Station> orderedStations = line.getStationsUpwardToDownward();
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                StationResponse.from(orderedStations),
                line.getExtraFare()
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
