package subway.line.application.dto;

public class LineCreateCommand {

    private final String lineName;

    public LineCreateCommand(final String lineName) {
        this.lineName = lineName;
    }

    public String lineName() {
        return lineName;
    }
}
