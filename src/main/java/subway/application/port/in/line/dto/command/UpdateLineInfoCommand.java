package subway.application.port.in.line.dto.command;

public class UpdateLineInfoCommand {

    private final long lineId;
    private final String name;
    private final String color;

    public UpdateLineInfoCommand(final long lineId, final String name, final String color) {
        this.lineId = lineId;
        this.name = name;
        this.color = color;
    }

    public long getLineId() {
        return lineId;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
