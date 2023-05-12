package subway.line.application.port.input;

import java.util.List;
import subway.ui.dto.response.LineResponse;

public interface FindAllLinesUseCase {
    List<LineResponse> findAllLines();
}
