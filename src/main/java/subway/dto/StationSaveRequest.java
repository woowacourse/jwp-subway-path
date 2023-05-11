package subway.dto;

import java.util.Objects;
import subway.domain.Station;

public class StationSaveRequest {

    private Long lineId;
    private String upStation;
    private String downStation;
    private int distance;

    public StationSaveRequest() {
    }

    public StationSaveRequest(final Long lineId, final String upStation, final String downStation, final int distance) {
        this.lineId = lineId;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Long getLineId() {
        return lineId;
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StationSaveRequest request = (StationSaveRequest) o;
        return Objects.equals(upStation, request.upStation) && Objects.equals(downStation, request.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStation, downStation);
    }
}
