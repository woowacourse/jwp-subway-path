package subway.dto;

import subway.domain.Station;

import java.util.List;

public class PathAndFee {

    private List<Station> stations;
    private int fee;

    public PathAndFee() {
    }

    public PathAndFee(List<Station> stations, int fee) {
        this.stations = stations;
        this.fee = fee;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getFee() {
        return fee;
    }
}
