package subway.application.core.service.dto.in;

import javax.validation.constraints.NotNull;

public class IdCommand {

    @NotNull
    private final Long id;

    public IdCommand(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
