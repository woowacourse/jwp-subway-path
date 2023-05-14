package subway.application.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import subway.application.line.port.in.LineNotFoundException;
import subway.application.line.port.in.LineUpdateInfoUseCase;
import subway.application.line.port.in.LineUpdateRequestDto;
import subway.application.line.port.out.LineRepository;
import subway.domain.line.Line;

@RequiredArgsConstructor
@Service
public class LineUpdateService implements LineUpdateInfoUseCase {

    private final LineRepository lineRepository;

    @Override
    public void updateLine(final LineUpdateRequestDto lineUpdateRequestDto) {
        final Line line = lineRepository.findById(lineUpdateRequestDto.getId())
            .orElseThrow(LineNotFoundException::new);
        line.updateColor(lineUpdateRequestDto.getColor());
        line.updateName(lineUpdateRequestDto.getName());
        lineRepository.update(line);
    }
}
