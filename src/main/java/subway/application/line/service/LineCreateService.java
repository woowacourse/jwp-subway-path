package subway.application.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.line.port.in.LineCreateRequestDto;
import subway.application.line.port.in.LineCreateUseCase;
import subway.application.line.port.in.LineResponseDto;
import subway.application.line.port.out.LineRepository;
import subway.domain.line.Line;

@RequiredArgsConstructor
@Service
@Transactional
public class LineCreateService implements LineCreateUseCase {

    private final LineRepository lineRepository;

    @Override
    public LineResponseDto createLine(final LineCreateRequestDto lineCreateRequestDto) {
        final Line line = new Line(lineCreateRequestDto.getName(), lineCreateRequestDto.getColor(),
            lineCreateRequestDto.getUpStationId(), lineCreateRequestDto.getDownStationId(),
            lineCreateRequestDto.getDistance());
        final Line savedLine = lineRepository.save(line);
        return LineResponseDto.from(savedLine);
    }
}
