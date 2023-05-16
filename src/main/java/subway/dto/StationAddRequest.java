package subway.dto;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class StationAddRequest {

    @NotBlank
    private final String lineName;

    @NotBlank
    private final String upLineStationName;

    @NotBlank
    private final String downLineStationName;

    @NotNull
    @Range(min = 1, max = 30)
    private final int distance;

    public StationAddRequest(final String lineName, final String upLineStationName, final String downLineStationName, final int distance) {
        this.lineName = lineName;
        this.upLineStationName = upLineStationName;
        this.downLineStationName = downLineStationName;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
    }

    public String getUpLineStationName() {
        return upLineStationName;
    }

    public String getDownLineStationName() {
        return downLineStationName;
    }

    public int getDistance() {
        return distance;
    }
}
