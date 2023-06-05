package subway.line.application;

import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.application.dto.request.LineAddInterStationRequestDto;
import subway.line.application.dto.request.LineCreateRequestDto;
import subway.line.application.dto.request.LineUpdateRequestDto;
import subway.line.application.dto.response.LineResponseDto;
import subway.line.application.exception.LineNotFoundException;
import subway.line.domain.Line;
import subway.line.domain.LineRepository;
import subway.station.domain.StationDeletedEvent;

@Service
@Transactional
public class LineCommandService {

    private final LineRepository lineRepository;

    public LineCommandService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponseDto createLine(LineCreateRequestDto lineCreateRequestDto) {
        Line line = new Line(lineCreateRequestDto.getName(), lineCreateRequestDto.getColor(),
                lineCreateRequestDto.getUpStationId(), lineCreateRequestDto.getDownStationId(),
                lineCreateRequestDto.getDistance());
        Line savedLine = lineRepository.save(line);
        return LineResponseDto.from(savedLine);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    public LineResponseDto addInterStation(LineAddInterStationRequestDto requestDto) {
        Line line = lineRepository.findById(requestDto.getId()).orElseThrow(LineNotFoundException::new);
        line.addInterStation(requestDto.getUpStationId(), requestDto.getDownStationId(), requestDto.getNewStationId(),
                requestDto.getDistance());
        Line result = lineRepository.update(line);
        return LineResponseDto.from(result);
    }

    @EventListener(StationDeletedEvent.class)
    public void removeStation(StationDeletedEvent event) {
        List<Line> lines = lineRepository.findAll();
        for (Line line : lines) {
            line.deleteStation(event.getId());
            Line result = lineRepository.update(line);
            removeIfNoStationInLine(result);
        }
    }

    private void removeIfNoStationInLine(Line result) {
        if (result.isEmpty()) {
            lineRepository.deleteById(result.getId());
        }
    }

    public void updateLine(LineUpdateRequestDto lineUpdateRequestDto) {
        Line line = lineRepository.findById(lineUpdateRequestDto.getId())
                .orElseThrow(LineNotFoundException::new);
        line.updateColor(lineUpdateRequestDto.getColor());
        line.updateName(lineUpdateRequestDto.getName());
        lineRepository.update(line);
    }
}
