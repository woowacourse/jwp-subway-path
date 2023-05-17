package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Subway;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.LineSaveResponse;
import subway.dto.SectionAddRequest;
import subway.dto.StationDeleteRequest;
import subway.exception.DuplicatedNameException;
import subway.repository.LineRepository;

@Transactional
@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineSaveResponse saveLine(LineRequest request) {
        if (lineRepository.existsByName(request.getLineName())) {
            throw new DuplicatedNameException(request.getLineName());
        }
        Line line = request.toDomain();
        Long saveId = lineRepository.save(line);
        return new LineSaveResponse(saveId);
    }

    public void addStation(Long lineId, SectionAddRequest request) {
        Subway subway = new Subway(lineRepository.findAll());
        Line line = lineRepository.findById(lineId);
        subway.addStation(
                line.getName(),
                request.getSourceStation(),
                request.getTargetStation(),
                request.getDistance()
        );
        lineRepository.save(subway.findLineByName(line.getName()));
    }

    public void deleteStation(Long lineId, StationDeleteRequest request) {
        Subway subway = new Subway(lineRepository.findAll());
        Line line = lineRepository.findById(lineId);
        subway.removeStation(line.getName(), request.getStationName());
        lineRepository.save(subway.findLineByName(line.getName()));
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        Subway subway = new Subway(lineRepository.findAll());
        return subway.getLines().stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long lineId) {
        Line line = lineRepository.findById(lineId);
        return LineResponse.from(line);
    }

    public void deleteLineById(Long lineId) {
        lineRepository.deleteById(lineId);
    }
}
