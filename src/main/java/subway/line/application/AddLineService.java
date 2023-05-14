package subway.line.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.port.input.AddLineUseCase;
import subway.line.application.port.output.LineRepository;
import subway.line.domain.Subway;
import subway.line.dto.LineSaveRequest;

@RequiredArgsConstructor
@Transactional
@Service
public class AddLineService implements AddLineUseCase {
    private final LineRepository lineRepository;
    
    @Override
    public Long addLine(final LineSaveRequest lineSaveRequest) {
        final Subway subway = new Subway(lineRepository.findAll());
        subway.addLine(lineSaveRequest.getName(), lineSaveRequest.getColor());
        
        return lineRepository.save(lineSaveRequest.toEntity());
    }
}
