package subway.service.dto;

public class DeleteStationFromLineCommand {

    private final String lineName;
    private final String deleteStationName;

    public DeleteStationFromLineCommand(final String lineName, final String deleteStationName) {
        this.lineName = lineName;
        this.deleteStationName = deleteStationName;
    }

    public String getLineName() {
        return lineName;
    }

    public String getDeleteStationName() {
        return deleteStationName;
    }
}
