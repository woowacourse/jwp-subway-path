package subway.application.line.port.in.findall;

import java.util.List;
import subway.application.line.port.in.LineResponseDto;

public interface LineFindAllUseCase {

    List<LineResponseDto> findAllLines();
}
