package subway.application;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import subway.ui.dto.LineRequest;
import subway.ui.dto.LineResponse;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

@Service
public class LineService {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public LineService(LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public LineResponse saveLine(LineRequest request) {
        Optional<Station> upStation = stationDao.findById(request.getUpStationId());
        Optional<Station> downStation = stationDao.findById(request.getDownStationId());
        validateStationsPresence(upStation, downStation);

        Line line = lineDao.insert(new Line(request.getName(), request.getColor()));
        sectionDao.insert(new Section(upStation.get(), downStation.get(), line, request.getDistance()));
        return LineResponse.of(line);
    }

    private void validateStationsPresence(Optional<Station> upStation, Optional<Station> downStation) {
        if (upStation.isEmpty() || downStation.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 역이 있습니다.");
        }
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
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }
}
