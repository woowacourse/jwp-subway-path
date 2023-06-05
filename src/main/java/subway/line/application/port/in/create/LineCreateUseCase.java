package subway.line.application.port.in.create;

import subway.line.application.port.in.LineResponseDto;

public interface LineCreateUseCase {

    LineResponseDto createLine(LineCreateRequestDto lineCreateRequestDto);
}
