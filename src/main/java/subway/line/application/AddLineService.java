package subway.line.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.port.input.AddLineUseCase;
import subway.line.application.port.output.GetAllLinePort;
import subway.line.application.port.output.SaveLinePort;
import subway.line.domain.Subway;
import subway.line.dto.AddLineRequest;

@Transactional
@Service
public class AddLineService implements AddLineUseCase {
    private final GetAllLinePort getAllLinePort;
    private final SaveLinePort saveLinePort;
    
    public AddLineService(final GetAllLinePort getAllLinePort, final SaveLinePort saveLinePort) {
        this.getAllLinePort = getAllLinePort;
        this.saveLinePort = saveLinePort;
    }
    
    @Override
    public Long addLine(final AddLineRequest addLineRequest) {
        final Subway subway = new Subway(getAllLinePort.getAll());
        subway.addLine(addLineRequest.getName(), addLineRequest.getColor(), addLineRequest.getExtraCharge());
        
        return saveLinePort.save(addLineRequest.toEntity());
    }
}
