package subway.dto;

import java.util.List;

public class SectionRequest {

    private final Long lineId;
    private final List<SectionStation> stations;

    public SectionRequest(final Long lineId, final List<SectionStation> stations) {
        this.lineId = lineId;
        this.stations = stations;
    }

    public Long getLineId() {
        return lineId;
    }

    public List<SectionStation> getStations() {
        return stations;
    }
}
