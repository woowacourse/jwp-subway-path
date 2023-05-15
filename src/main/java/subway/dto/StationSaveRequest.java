package subway.dto;

import subway.domain.Station;

public class StationSaveRequest {

    private String upStation;
    private String downStation;
    private int distance;

    public StationSaveRequest() {
    }

    public StationSaveRequest(final String upStation, final String downStation, final int distance) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }


    public String getUpStation() {
        return upStation;
    }

    public String getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }

    public Station getUpStationEntity() {
        return Station.of(upStation);
    }

    public Station getDownStationEntity() {
        return Station.of(downStation);
    }

}
