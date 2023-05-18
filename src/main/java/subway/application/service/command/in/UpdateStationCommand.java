package subway.application.service.command.in;

import subway.application.domain.Station;
import subway.application.service.command.SelfValidating;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UpdateStationCommand extends SelfValidating<UpdateStationCommand> {

    @NotNull
    private final Long id;
    @NotEmpty
    private final String name;

    public UpdateStationCommand(Long id, String name) {
        this.id = id;
        this.name = name;
        validateSelf();
    }

    public Station toEntity() {
        return new Station(id, name);
    }
}
