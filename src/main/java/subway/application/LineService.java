package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.StationEnrollRequest;
import subway.entity.LineEntity;
import subway.exception.NoLineException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineDao lineDao;
    private final SectionService sectionService;
    private final StationService stationService;

    public LineService(final LineDao lineDao, final SectionService sectionService, final StationService stationService) {
        this.lineDao = lineDao;
        this.sectionService = sectionService;
        this.stationService = stationService;
    }

    public Line saveLine(final LineRequest request) {
        return lineDao.insert(new LineEntity(request.getName(), request.getColor())).toLine();
    }

    @Transactional(readOnly = true)
    public List<Line> findLines() {
        return lineDao.findAll().stream()
                .map(LineEntity::toLine)
                .collect(Collectors.toList());
    }

    private LineEntity findLine(final Long lineId) {
        return lineDao.findById(lineId)
                .orElseThrow(() -> new NoLineException(lineId + "를 가진 라인은 없습니다."));
    }

    @Transactional(readOnly = true)
    public Line findLineById(final Long lineId) {
        return findLine(lineId).toLine();
    }

    public Line updateLine(final Long lineId, final LineRequest lineUpdateRequest) {
        final LineEntity line = findLine(lineId);
        final LineEntity newLine = new LineEntity(line.getId(), lineUpdateRequest.getName(), lineUpdateRequest.getColor());
        lineDao.update(newLine);
        return newLine.toLine();
    }

    public void deleteLineById(final Long lineId) {
        final LineEntity line = findLine(lineId);
        lineDao.deleteById(line.getId());
    }

    public Section enrollStation(final Long lineId, final StationEnrollRequest request) {
        final Line line = makeLineWithStationsBy(lineId);
        final Station left = stationService.findById(request.getFromStation());
        final Station right = stationService.findById(request.getToStation());
        final Section section = new Section(left, right, new Distance(request.getDistance()));
        line.addSection(section);
        sectionService.save(line);
        return section;
    }

    private Line makeLineWithStationsBy(final Long lineId) {
        final Line line = findLineById(lineId);
        final List<Section> sections = sectionService.findSectionsById(lineId);
        return new Line(line.getId(), line.getName(), line.getColor(), sections);
    }

    public void deleteStation(final Long lineId, final Long stationId) {
        final Line line = makeLineWithStationsBy(lineId);
        line.deleteStation(stationService.findById(stationId));
        sectionService.save(line);
    }

    @Transactional(readOnly = true)
    public Line findLineWithStationsById(final Long lineId) {
        return makeLineWithStationsBy(lineId);
    }

    @Transactional(readOnly = true)
    public List<Line> findAllRouteMap() {
        final List<Line> linesWithStations = new ArrayList<>();
        for (final Line line : findLines()) {
            linesWithStations.add(makeLineWithStationsBy(line.getId()));
        }
        return linesWithStations;
    }

}
