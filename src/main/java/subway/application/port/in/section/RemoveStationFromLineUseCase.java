package subway.application.port.in.section;

import subway.application.port.in.section.dto.command.RemoveStationFromLineCommand;

public interface RemoveStationFromLineUseCase {

    void removeStation(RemoveStationFromLineCommand command);
}
