package subway.application.dto;

public class LineCreateCommand {

    private String lineName;
    private String upTerminalName;
    private String downTerminalName;
    private int distance;

    public LineCreateCommand(final String lineName, final String upTerminalName, final String downTerminalName,
                             final int distance) {
        this.lineName = lineName;
        this.upTerminalName = upTerminalName;
        this.downTerminalName = downTerminalName;
        this.distance = distance;
    }

    public String getLineName() {
        return lineName;
    }

    public String getUpTerminalName() {
        return upTerminalName;
    }

    public String getDownTerminalName() {
        return downTerminalName;
    }

    public int getDistance() {
        return distance;
    }
}
