package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.exception.DuplicatedNameException;
import subway.repository.LineRepository;
import subway.repository.StationRepository;
import subway.repository.dao.LineDao;

@Service
public class LineService {
    private final LineDao lineDao;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineDao lineDao, LineRepository lineRepository,
                       StationRepository stationRepository) {
        this.lineDao = lineDao;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        String name = request.getLineName();
        if (lineDao.existsByName(name)) {
            throw new DuplicatedNameException(name);
        }

        Station source = stationRepository.findOrSaveStation(request.getSourceStation());
        Station target = stationRepository.findOrSaveStation(request.getTargetStation());

        Section section = new Section(source, target, request.getDistance());
        Line line = new Line(name, List.of(section));
        Line persistLine = lineRepository.save(line);
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findLineResponses() {
        List<Line> persistLines = findLines();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        return lineDao.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        Line persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    public Line findLineById(Long id) {
        return lineDao.findById(id);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
//        lineDao.update(new Line(id, lineUpdateRequest.getName()));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }

}
