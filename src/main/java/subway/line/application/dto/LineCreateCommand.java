package subway.line.application.dto;

public class LineCreateCommand {

    private final String lineName;
    private final int surcharge;

    public LineCreateCommand(final String lineName, final int surcharge) {
        this.lineName = lineName;
        this.surcharge = surcharge;
    }

    public String lineName() {
        return lineName;
    }

    public int surcharge() {
        return surcharge;
    }
}
