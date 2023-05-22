package subway.line.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.port.input.GetSortedLineUseCase;
import subway.line.application.port.output.GetAllLinePort;
import subway.line.application.port.output.GetLineByIdPort;
import subway.line.domain.Line;
import subway.line.domain.Subway;
import subway.line.dto.GetSortedLineResponse;

@Transactional(readOnly = true)
@Service
public class GetSortedLineService implements GetSortedLineUseCase {
    private final GetAllLinePort getAllLinePort;
    private final GetLineByIdPort getLineByIdPort;
    
    public GetSortedLineService(final GetAllLinePort getAllLinePort, final GetLineByIdPort getLineByIdPort) {
        this.getAllLinePort = getAllLinePort;
        this.getLineByIdPort = getLineByIdPort;
    }
    
    @Override
    public GetSortedLineResponse getSortedLine(final Long lineId) {
        final Subway subway = new Subway(getAllLinePort.getAll());
        final Line lineById = getLineByIdPort.getLineById(lineId);
        return new GetSortedLineResponse(
                lineById.getName(),
                lineById.getColor(),
                lineById.getExtraCharge(),
                subway.getSortedStations(lineById.getName())
        );
    }
}
