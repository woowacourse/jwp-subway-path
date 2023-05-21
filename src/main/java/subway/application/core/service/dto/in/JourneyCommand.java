package subway.application.core.service.dto.in;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class JourneyCommand {

    @NotNull
    private final Long departure;
    @NotNull
    private final Long terminal;

    public JourneyCommand(Long departure, Long terminal) {
        this.departure = departure;
        this.terminal = terminal;
    }

    public Long getDeparture() {
        return departure;
    }

    public Long getTerminal() {
        return terminal;
    }
}
