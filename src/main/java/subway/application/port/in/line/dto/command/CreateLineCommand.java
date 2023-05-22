package subway.application.port.in.line.dto.command;

public class CreateLineCommand {

    private final String name;

    private final String color;

    private final Integer surcharge;

    public CreateLineCommand(final String name, final String color, final Integer surcharge) {
        this.name = name;
        this.color = color;
        this.surcharge = surcharge;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Integer getSurcharge() {
        return surcharge;
    }
}
