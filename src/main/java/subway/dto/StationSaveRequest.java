package subway.dto;

import subway.domain.Station;

import java.util.Objects;

public class StationSaveRequest {

    private final String upStation;
    private final String downStation;
    private final int distance;

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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final StationSaveRequest that = (StationSaveRequest) o;
        return distance == that.distance && Objects.equals(upStation, that.upStation) && Objects.equals(downStation, that.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStation, downStation, distance);
    }

}
