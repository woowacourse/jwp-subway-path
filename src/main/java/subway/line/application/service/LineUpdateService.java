package subway.line.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.port.in.LineNotFoundException;
import subway.line.application.port.in.update.LineUpdateInfoUseCase;
import subway.line.application.port.in.update.LineUpdateRequestDto;
import subway.line.application.port.out.LineRepository;
import subway.line.domain.Line;

@Service
@Transactional
public class LineUpdateService implements LineUpdateInfoUseCase {

    private final LineRepository lineRepository;

    public LineUpdateService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Override
    public void updateLine(LineUpdateRequestDto lineUpdateRequestDto) {
        Line line = lineRepository.findById(lineUpdateRequestDto.getId())
                .orElseThrow(LineNotFoundException::new);
        line.updateColor(lineUpdateRequestDto.getColor());
        line.updateName(lineUpdateRequestDto.getName());
        lineRepository.update(line);
    }
}
