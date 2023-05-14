package subway.application.port.in.station;

import subway.application.port.in.station.dto.command.CreateStationCommand;

public interface CreateStationUseCase {

    long createStation(CreateStationCommand command);
}
