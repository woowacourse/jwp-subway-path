package subway.line.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.port.input.DeleteLineUseCase;
import subway.line.application.port.output.DeleteLinePort;
import subway.line.application.port.output.GetAllLinePort;
import subway.line.application.port.output.GetLineByIdPort;
import subway.line.domain.Line;
import subway.line.domain.Subway;
import subway.section.application.port.output.DeleteSectionByLineIdPort;

@RequiredArgsConstructor
@Transactional
@Service
public class DeleteLineService implements DeleteLineUseCase {
    private final GetAllLinePort getAllLinePort;
    private final GetLineByIdPort getLineByIdPort;
    private final DeleteLinePort deleteLinePort;
    private final DeleteSectionByLineIdPort deleteSectionByLineIdPort;
    
    @Override
    public void deleteLine(final Long lineId) {
        final Subway subway = new Subway(getAllLinePort.getAll());
        final Line lineById = getLineByIdPort.getLineById(lineId);
        subway.removeLine(lineById.getName());
        
        deleteLinePort.deleteById(lineId);
        deleteSectionByLineIdPort.deleteSectionByLineId(lineId);
    }
}
