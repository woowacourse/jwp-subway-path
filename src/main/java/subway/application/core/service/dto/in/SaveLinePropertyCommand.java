package subway.application.core.service.dto.in;

import subway.application.core.domain.LineProperty;

import javax.validation.constraints.NotEmpty;

public class SaveLinePropertyCommand {

    private final Long id;
    @NotEmpty
    private final String name;
    @NotEmpty
    private final String color;

    public SaveLinePropertyCommand(String name, String color) {
        this.id = null;
        this.name = name;
        this.color = color;
    }

    public LineProperty toEntity() {
        return new LineProperty(id, name, color);
    }
}
