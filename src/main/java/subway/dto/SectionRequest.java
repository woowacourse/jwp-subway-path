package subway.dto;

import java.util.List;

public class SectionRequest {

    private final Long lineId;
    private final List<SectionStation> stations;
    private final Integer distance;

    public SectionRequest(final Long lineId, final List<SectionStation> stations, final Integer distance) {
        this.lineId = lineId;
        this.stations = stations;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
    }

    public List<SectionStation> getStations() {
        return stations;
    }

    public Integer getDistance() {
        return distance;
    }
}
