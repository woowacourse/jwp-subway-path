package subway.application.dto;

import java.util.List;

public class LineResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final int extraFare;
    private final List<StationResponse> stationResponses;

    public LineResponse(final Long id, final String name, final String color,
                        final int extraFare, final List<StationResponse> stationResponses) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.extraFare = extraFare;
        this.stationResponses = stationResponses;
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

    public List<StationResponse> getStationResponses() {
        return stationResponses;
    }
}
