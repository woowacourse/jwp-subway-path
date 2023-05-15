package subway.line.application.dto;

public class AddStationToLineCommand {

    private final String lineName;
    private final String upStationName;
    private final String downStationName;
    private final int distance;

    public AddStationToLineCommand(final String lineName,
                                   final String upStationName,
                                   final String downStationName,
                                   final int distance) {
        this.lineName = lineName;
        this.upStationName = upStationName;
        this.downStationName = downStationName;
        this.distance = distance;
    }

    public String lineName() {
        return lineName;
    }

    public String upStationName() {
        return upStationName;
    }

    public String downStationName() {
        return downStationName;
    }

    public int distance() {
        return distance;
    }
}
