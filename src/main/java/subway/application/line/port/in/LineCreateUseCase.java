package subway.application.line.port.in;

public interface LineCreateUseCase {

    LineCreateResponseDto createLine(LineCreateRequestDto lineCreateRequestDto);
}
