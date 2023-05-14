package subway.application.line.port.in;

public interface LineCreateUseCase {

    LineResponseDto createLine(LineCreateRequestDto lineCreateRequestDto);
}
