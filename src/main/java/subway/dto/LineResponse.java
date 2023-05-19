package subway.dto;

import java.util.ArrayList;
import java.util.List;
import subway.domain.Line;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private Integer additionalFee;
    private List<StationResponse> stations;

    LineResponse() {
    }

    public LineResponse(final Long id, final String name, final String color, final Integer additionalFee,
        final List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.additionalFee = additionalFee;
        this.stations = stations;
    }

    public static LineResponse of(final Line line) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getAdditionalFee(),
            new ArrayList<>());
    }

    public static LineResponse withStationResponses(final Line line, final List<StationResponse> stations) {
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getAdditionalFee(), stations);
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

    public Integer getAdditionalFee() {
        return additionalFee;
    }
}
