package subway.application.core.service.dto.in;

import subway.application.core.domain.Station;

import javax.validation.constraints.NotEmpty;

public class SaveStationCommand {

    private final Long id;
    @NotEmpty
    private final String name;

    public SaveStationCommand(String name) {
        this.id = null;
        this.name = name;
    }

    public Station toEntity() {
        return new Station(id, name);
    }
}
