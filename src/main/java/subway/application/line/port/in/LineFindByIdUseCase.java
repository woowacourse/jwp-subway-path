package subway.application.line.port.in;

public interface LineFindByIdUseCase {

    LineResponseDto findById(Long id);
}
