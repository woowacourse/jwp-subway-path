package subway.line.application.dto;

public class DeleteStationFromLineCommand {

    private final String lineName;
    private final String deleteStationName;

    public DeleteStationFromLineCommand(final String lineName, final String deleteStationName) {
        this.lineName = lineName;
        this.deleteStationName = deleteStationName;
    }

    public String lineName() {
        return lineName;
    }

    public String deleteStationName() {
        return deleteStationName;
    }
}
