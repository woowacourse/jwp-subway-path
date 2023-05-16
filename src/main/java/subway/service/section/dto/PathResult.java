package subway.service.section.dto;

import subway.service.station.dto.StationResponse;

import java.util.List;

public class PathResult {
    private int fee;
    private List<StationResponse> stations;

    public PathResult(int fee, List<StationResponse> stations) {
        this.fee = fee;
        this.stations = stations;
    }

    public PathResult() {
    }

    public int getFee() {
        return fee;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
