package subway.application.line.port.in;

import java.util.List;

public interface LineFindAllUseCase {

    List<LineResponseDto> findAllLines();
}
