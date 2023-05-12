package subway.application.dto;

public class LineCreateCommand {

    private final String lineName;
    private final String upTerminalName;
    private final String downTerminalName;
    private final int distance;

    public LineCreateCommand(final String lineName,
                             final String upTerminalName,
                             final String downTerminalName,
                             final int distance) {
        this.lineName = lineName;
        this.upTerminalName = upTerminalName;
        this.downTerminalName = downTerminalName;
        this.distance = distance;
    }

    public String lineName() {
        return lineName;
    }

    public String upTerminalName() {
        return upTerminalName;
    }

    public String downTerminalName() {
        return downTerminalName;
    }

    public int distance() {
        return distance;
    }
}
