package subway.application.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.line.port.in.LineNotFoundException;
import subway.application.line.port.in.update.LineUpdateInfoUseCase;
import subway.application.line.port.in.update.LineUpdateRequestDto;
import subway.application.line.port.out.LineRepository;
import subway.domain.line.Line;

@Service
@Transactional
public class LineUpdateService implements LineUpdateInfoUseCase {

    private final LineRepository lineRepository;

    public LineUpdateService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Override
    public void updateLine(final LineUpdateRequestDto lineUpdateRequestDto) {
        final Line line = lineRepository.findById(lineUpdateRequestDto.getId())
                .orElseThrow(LineNotFoundException::new);
        line.updateColor(lineUpdateRequestDto.getColor());
        line.updateName(lineUpdateRequestDto.getName());
        lineRepository.update(line);
    }
}
