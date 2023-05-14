package subway.application.port.in.line.dto.command;

public class CreateLineCommand {

    private final String name;

    private final String color;

    public CreateLineCommand(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }
}
