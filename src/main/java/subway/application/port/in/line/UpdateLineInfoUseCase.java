package subway.application.port.in.line;

import subway.application.port.in.line.dto.command.UpdateLineInfoCommand;

public interface UpdateLineInfoUseCase {

    void updateLineInfo(UpdateLineInfoCommand command);
}
