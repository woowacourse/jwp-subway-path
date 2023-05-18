package subway.application.service.command.in;

import subway.application.domain.LineProperty;
import subway.application.service.command.SelfValidating;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UpdateLinePropertyCommand extends SelfValidating<UpdateLinePropertyCommand> {

    @NotNull
    private final Long id;
    @NotEmpty
    private final String name;
    @NotEmpty
    private final String color;

    public UpdateLinePropertyCommand(Long id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
        validateSelf();
    }

    public LineProperty toEntity() {
        return new LineProperty(id, name, color);
    }
}
