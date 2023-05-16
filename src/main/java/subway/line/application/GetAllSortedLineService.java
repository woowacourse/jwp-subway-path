package subway.line.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.port.input.GetAllSortedLineUseCase;
import subway.line.application.port.output.GetAllLinePort;
import subway.line.domain.Subway;
import subway.line.dto.GetAllSortedLineResponse;

@RequiredArgsConstructor
@Transactional
@Service
public class GetAllSortedLineService implements GetAllSortedLineUseCase {
    private final GetAllLinePort getAllLinePort;
    
    @Override
    public GetAllSortedLineResponse getAllSortedLine() {
        final Subway subway = new Subway(getAllLinePort.getAll());
        return subway.getAllSortedStations();
    }
}
