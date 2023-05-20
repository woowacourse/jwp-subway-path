package subway.dto;

import java.util.ArrayList;
import java.util.List;
import subway.domain.Line;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private Integer additionalFare;
    private List<StationResponse> stations;

    LineResponse() {
    }

    public LineResponse(final Long id, final String name, final String color, final Integer additionalFare,
        final List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.additionalFare = additionalFare;
        this.stations = stations;
    }

    public static LineResponse of(final Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getAdditionalFare(),
            new ArrayList<>());
    }

    public static LineResponse withStationResponses(final Line line, final List<StationResponse> stations) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getAdditionalFare(), stations);
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

    public Integer getAdditionalFare() {
        return additionalFare;
    }
}
