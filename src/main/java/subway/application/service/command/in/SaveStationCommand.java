package subway.application.service.command.in;

import subway.application.domain.Station;
import subway.application.service.command.SelfValidating;

import javax.validation.constraints.NotEmpty;

public class SaveStationCommand extends SelfValidating<SaveStationCommand> {

    private final Long id;
    @NotEmpty
    private final String name;

    public SaveStationCommand(String name) {
        this.id = null;
        this.name = name;
        validateSelf();
    }

    public Station toEntity() {
        return new Station(id, name);
    }
}
