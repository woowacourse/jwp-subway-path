package subway.line.application.port.in.findall;

import java.util.List;
import subway.line.application.port.in.LineResponseDto;

public interface LineFindAllUseCase {

    List<LineResponseDto> findAllLines();
}
