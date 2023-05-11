package subway.dto;

import java.util.List;

public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stationResponses;

    private LineResponse() {
        this(null, null, null, null);
    }

    private LineResponse(final Long id, final String name, final String color,
                         final List<StationResponse> stationResponses) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.stationResponses = stationResponses;
    }

    public static LineResponse of(final Long lineId, final String lineName, final String lineColor,
                                  final List<StationResponse> stationResponses) {
        return new LineResponse(lineId, lineName, lineColor, stationResponses);
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

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }
}
