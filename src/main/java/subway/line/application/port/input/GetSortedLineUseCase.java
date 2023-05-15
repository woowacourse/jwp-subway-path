package subway.line.application.port.input;

import java.util.List;

public interface GetSortedLineUseCase {
    List<String> getSortedLine(Long lineId);
}
