package subway.application.port.in.section;

import subway.application.port.in.section.dto.command.AddStationToLineCommand;

public interface AddStationToLineUseCase {

    void addStation(AddStationToLineCommand command);
}
