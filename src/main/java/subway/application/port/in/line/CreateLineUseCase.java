package subway.application.port.in.line;

import subway.application.port.in.line.dto.command.CreateLineCommand;

public interface CreateLineUseCase {

    long createLine(CreateLineCommand command);
}
