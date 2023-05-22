package subway.application;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.Subway;
import subway.dto.LineRequest;
import subway.dto.SectionAddRequest;
import subway.dto.StationDeleteRequest;
import subway.exception.DuplicatedNameException;
import subway.repository.LineRepository;

@Transactional
@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationService stationService;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    public Line saveLine(LineRequest request) {
        if (lineRepository.existsByName(request.getLineName())) {
            throw new DuplicatedNameException(request.getLineName());
        }
        return lineRepository.save(request.toDomain());
    }

    public void addSection(Long lineId, SectionAddRequest request) {
        Subway subway = new Subway(lineRepository.findAll());
        Line line = findLineById(lineId);
        Station source = stationService.findByName(request.getSourceStation());
        Station target = stationService.findByName(request.getTargetStation());
        subway.addStation(
                line.getName(),
                source,
                target,
                request.getDistance()
        );
        lineRepository.save(subway.findLineByName(line.getName()));
    }

    public void deleteStation(Long lineId, StationDeleteRequest request) {
        Subway subway = new Subway(lineRepository.findAll());
        Line line = findLineById(lineId);
        subway.removeStation(line.getName(), request.getStationName());
        lineRepository.save(subway.findLineByName(line.getName()));
    }

    @Transactional(readOnly = true)
    public List<Line> findAllLines() {
        Subway subway = new Subway(lineRepository.findAll());
        return subway.getLines();
    }

    @Transactional(readOnly = true)
    public Line findLineById(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new NoSuchElementException("노선이 없습니다"));
    }

    public void deleteLineById(Long lineId) {
        lineRepository.deleteById(lineId);
    }
}
