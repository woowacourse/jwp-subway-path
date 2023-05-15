package subway.line.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.port.input.AddLineUseCase;
import subway.line.application.port.output.FindAllLinePort;
import subway.line.application.port.output.SaveLinePort;
import subway.line.domain.Subway;
import subway.line.dto.LineSaveRequest;

@RequiredArgsConstructor
@Transactional
@Service
public class AddLineService implements AddLineUseCase {
    private final FindAllLinePort findAllLinePort;
    private final SaveLinePort saveLinePort;
    
    @Override
    public Long addLine(final LineSaveRequest lineSaveRequest) {
        final Subway subway = new Subway(findAllLinePort.findAll());
        subway.addLine(lineSaveRequest.getName(), lineSaveRequest.getColor());
        
        return saveLinePort.save(lineSaveRequest.toEntity());
    }
}
