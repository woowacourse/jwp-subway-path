package subway.presentation.request;

import subway.application.dto.AddStationToLineCommand;
import subway.domain.Direction;

public class AddStationToLineRequest {

    private String lineName;
    private String standardStationName;
    private Direction direction;
    private String addedStationName;
    private Integer distance;

    private AddStationToLineRequest() {
    }

    public AddStationToLineRequest(final String lineName, final String standardStationName, final Direction direction,
                                   final String addedStationName, final Integer distance) {
        this.lineName = lineName;
        this.standardStationName = standardStationName;
        this.direction = direction;
        this.addedStationName = addedStationName;
        this.distance = distance;
    }

    public AddStationToLineCommand toCommand() {
        return null;
    }
}
