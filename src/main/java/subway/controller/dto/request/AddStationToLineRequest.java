package subway.controller.dto.request;

import javax.validation.constraints.NotBlank;
import subway.controller.dto.AddStationLocation;

public class AddStationToLineRequest {

    @NotBlank
    private AddStationLocation addStationLocation;
    @NotBlank
    private String lineName;
    @NotBlank
    private String stationName;
    private String upStationName;
    private String downStationName;
    @NotBlank
    private Long distance;

    private AddStationToLineRequest() {
    }

    public AddStationToLineRequest(final AddStationLocation addStationLocation, final String lineName,
        final String stationName,
        final String upStationName, final String downStationName, final Long distance) {
        this.addStationLocation = addStationLocation;
        this.lineName = lineName;
        this.stationName = stationName;
        this.upStationName = upStationName;
        this.downStationName = downStationName;
        this.distance = distance;
    }

    public AddStationLocation getAddStationLocation() {
        return addStationLocation;
    }

    public String getLineName() {
        return lineName;
    }

    public String getStationName() {
        return stationName;
    }

    public String getUpStationName() {
        return upStationName;
    }

    public String getDownStationName() {
        return downStationName;
    }

    public Long getDistance() {
        return distance;
    }
}
