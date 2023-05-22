package subway.application.core.service.dto.in;

import subway.application.core.domain.Station;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class UpdateStationCommand {

    @NotNull
    private final Long id;
    @NotEmpty
    private final String name;

    public UpdateStationCommand(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Station toEntity() {
        return new Station(id, name);
    }
}
