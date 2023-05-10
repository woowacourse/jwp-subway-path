package subway.presentation.request;

import subway.application.dto.DeleteStationFromLineCommand;

public class DeleteStationFromLineRequest {

    private String lineName;
    private String deleteStationName;

    private DeleteStationFromLineRequest() {
    }

    public DeleteStationFromLineRequest(final String lineName, final String deleteStationName) {
        this.lineName = lineName;
        this.deleteStationName = deleteStationName;
    }

    public DeleteStationFromLineCommand toCommand() {
        return new DeleteStationFromLineCommand(lineName, deleteStationName);
    }

    public String getLineName() {
        return lineName;
    }

    public String getDeleteStationName() {
        return deleteStationName;
    }
}
