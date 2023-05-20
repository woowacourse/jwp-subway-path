package subway.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.Subway;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.LineSaveResponse;
import subway.dto.SectionAddRequest;
import subway.dto.StationDeleteRequest;
import subway.exception.DuplicatedNameException;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Transactional
@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineSaveResponse saveLine(LineRequest request) {
        if (lineRepository.existsByName(request.getLineName())) {
            throw new DuplicatedNameException(request.getLineName());
        }
        Line savedLine = lineRepository.save(request.toDomain());
        return new LineSaveResponse(savedLine.getId());
    }

    public void addSection(Long lineId, SectionAddRequest request) {
        Subway subway = new Subway(lineRepository.findAll());
        Line line = findById(lineId);
        Station source = stationRepository.findByName(request.getSourceStation())
                .orElseThrow(() -> new NoSuchElementException("역을 찾을 수 없습니다"));
        Station target = stationRepository.findByName(request.getTargetStation())
                .orElseThrow(() -> new NoSuchElementException("역을 찾을 수 없습니다"));
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
        Line line = findById(lineId);
        subway.removeStation(line.getName(), request.getStationName());
        lineRepository.save(subway.findLineByName(line.getName()));
    }

    private Line findById(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new NoSuchElementException("노선이 없습니다"));
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
        Line line = findById(lineId);
        return LineResponse.from(line);
    }

    public void deleteLineById(Long lineId) {
        lineRepository.deleteById(lineId);
    }
}
