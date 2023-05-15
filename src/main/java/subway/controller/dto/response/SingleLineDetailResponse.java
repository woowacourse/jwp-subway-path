package subway.controller.dto.response;

import subway.service.domain.Stations;
import subway.service.domain.Station;

import java.util.List;

public class SingleLineDetailResponse {

    private final long lineId;
    private final String name;
    private final String color;
    private final List<Station> stations;

    public SingleLineDetailResponse(final long lineId, final String name, final String color, final Stations stations) {
        this.lineId = lineId;
        this.name = name;
        this.color = color;
        this.stations = stations.getStations();
    }

    public long getLineId() {
        return lineId;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Station> getStations() {
        return stations;
    }

}
