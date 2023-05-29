package subway.application.service.line;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.port.in.line.FindAllLinesUseCase;
import subway.application.port.in.line.FindLineByIdUseCase;
import subway.application.port.in.line.dto.response.LineQueryResponse;
import subway.application.port.out.line.LoadLinePort;
import subway.application.service.exception.NoSuchLineException;
import subway.common.mapper.LineMapper;
import subway.domain.line.Line;

@Service
@Transactional(readOnly = true)
public class LineQueryService implements FindLineByIdUseCase, FindAllLinesUseCase {

    private final LoadLinePort loadLinePort;

    public LineQueryService(final LoadLinePort loadLinePort) {
        this.loadLinePort = loadLinePort;
    }

    @Override
    public LineQueryResponse findLineById(final long lineId) {
        Line line = loadLinePort.findById(lineId)
                .orElseThrow(NoSuchLineException::new);
        return LineMapper.toResponse(line);
    }

    @Override
    public List<LineQueryResponse> findAllLines() {
        List<Line> lines = loadLinePort.findAll();
        return lines.stream()
                .map(LineMapper::toResponse)
                .collect(Collectors.toList());
    }
}
