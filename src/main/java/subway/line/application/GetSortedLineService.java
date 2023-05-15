package subway.line.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.port.input.GetSortedLineUseCase;
import subway.line.application.port.output.GetAllLinePort;
import subway.line.application.port.output.GetLineByIdPort;
import subway.line.domain.Line;
import subway.line.domain.Subway;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class GetSortedLineService implements GetSortedLineUseCase {
    private final GetAllLinePort getAllLinePort;
    private final GetLineByIdPort getLineByIdPort;
    
    @Override
    public List<String> getSortedLine(final Long lineId) {
        final Subway subway = new Subway(getAllLinePort.getAll());
        final Line lineById = getLineByIdPort.getLineById(lineId);
        return subway.getSortedStations(lineById.getName());
    }
}
