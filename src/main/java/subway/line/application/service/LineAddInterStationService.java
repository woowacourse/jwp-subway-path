package subway.line.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.port.in.LineNotFoundException;
import subway.line.application.port.in.LineResponseDto;
import subway.line.application.port.in.addinterstation.LineAddInterStationRequestDto;
import subway.line.application.port.in.addinterstation.LineAddInterStationUseCase;
import subway.line.application.port.out.LineRepository;
import subway.line.domain.Line;

@Service
@Transactional
public class LineAddInterStationService implements LineAddInterStationUseCase {

    private final LineRepository lineRepository;

    public LineAddInterStationService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Override
    public LineResponseDto addInterStation(LineAddInterStationRequestDto requestDto) {
        Line line = lineRepository.findById(requestDto.getId()).orElseThrow(LineNotFoundException::new);
        line.addInterStation(requestDto.getUpStationId(), requestDto.getDownStationId(), requestDto.getNewStationId(),
                requestDto.getDistance());
        Line result = lineRepository.update(line);
        return LineResponseDto.from(result);
    }

}
