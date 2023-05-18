package subway.application.service.command.in;

import subway.application.domain.LineProperty;
import subway.application.service.command.SelfValidating;

import javax.validation.constraints.NotEmpty;

public class SaveLinePropertyCommand extends SelfValidating<SaveLinePropertyCommand> {

    private final Long id;
    @NotEmpty
    private final String name;
    @NotEmpty
    private final String color;

    public SaveLinePropertyCommand(String name, String color) {
        this.id = null;
        this.name = name;
        this.color = color;
        validateSelf();
    }

    public LineProperty toEntity() {
        return new LineProperty(id, name, color);
    }
}
