package subway.application.port.in.line;

import java.util.List;
import subway.application.port.in.line.dto.response.LineQueryResponse;

public interface FindAllLinesUseCase {

    List<LineQueryResponse> findAllLines();
}
