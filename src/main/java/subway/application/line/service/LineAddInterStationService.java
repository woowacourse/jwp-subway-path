package subway.application.line.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.line.port.in.LineAddInterStationRequestDto;
import subway.application.line.port.in.LineAddInterStationUseCase;
import subway.application.line.port.in.LineNotFoundException;
import subway.application.line.port.in.LineResponseDto;
import subway.application.line.port.out.LineRepository;
import subway.domain.line.Line;

@RequiredArgsConstructor
@Service
@Transactional
public class LineAddInterStationService implements LineAddInterStationUseCase {

    private final LineRepository lineRepository;

    @Override
    public LineResponseDto addInterStation(final LineAddInterStationRequestDto requestDto) {
        final Line line = lineRepository.findById(requestDto.getId()).orElseThrow(LineNotFoundException::new);
        line.addInterStation(requestDto.getUpStationId(), requestDto.getDownStationId(), requestDto.getNewStationId(),
            requestDto.getDistance());
        final Line result = lineRepository.update(line);
        return LineResponseDto.from(result);
    }

}
