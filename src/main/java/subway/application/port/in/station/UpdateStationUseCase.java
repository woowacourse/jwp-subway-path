package subway.application.port.in.station;

import subway.application.port.in.station.dto.command.UpdateStationCommand;

public interface UpdateStationUseCase {

    void updateStation(UpdateStationCommand command);
}
