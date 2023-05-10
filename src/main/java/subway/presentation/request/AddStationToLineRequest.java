package subway.presentation.request;

import subway.application.dto.AddStationToLineCommand;

public class AddStationToLineRequest {

    private String lineName;
    private String upStationName;
    private String downStationName;
    private Integer distance;

    private AddStationToLineRequest() {
    }

    public AddStationToLineRequest(final String lineName, final String upStationName,
                                   final String downStationName, final Integer distance) {
        this.lineName = lineName;
        this.upStationName = upStationName;
        this.downStationName = downStationName;
        this.distance = distance;
    }

    public AddStationToLineCommand toCommand() {
        return new AddStationToLineCommand(lineName, upStationName, downStationName, distance);
    }

    public String getLineName() {
        return lineName;
    }

    public String getUpStationName() {
        return upStationName;
    }

    public String getDownStationName() {
        return downStationName;
    }

    public Integer getDistance() {
        return distance;
    }
}
