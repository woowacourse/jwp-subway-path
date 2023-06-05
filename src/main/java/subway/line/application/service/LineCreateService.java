package subway.line.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.port.in.LineResponseDto;
import subway.line.application.port.in.create.LineCreateRequestDto;
import subway.line.application.port.in.create.LineCreateUseCase;
import subway.line.application.port.out.LineRepository;
import subway.line.domain.Line;

@Service
@Transactional
public class LineCreateService implements LineCreateUseCase {

    private final LineRepository lineRepository;

    public LineCreateService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Override
    public LineResponseDto createLine(LineCreateRequestDto lineCreateRequestDto) {
        Line line = new Line(lineCreateRequestDto.getName(), lineCreateRequestDto.getColor(),
                lineCreateRequestDto.getUpStationId(), lineCreateRequestDto.getDownStationId(),
                lineCreateRequestDto.getDistance());
        Line savedLine = lineRepository.save(line);
        return LineResponseDto.from(savedLine);
    }
}
