package subway.application.dto;

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

    public String getLineName() {
        return lineName;
    }

    public String getUpStationName() {
        return upStationName;
    }

    public String getDownStationName() {
        return downStationName;
    }

    public int getDistance() {
        return distance;
    }
}
