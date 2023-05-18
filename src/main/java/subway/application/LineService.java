package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.SectionResponse;
import subway.dto.StationEnrollRequest;
import subway.entity.SectionEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationService stationService;

    public LineService(final LineDao lineDao, final SectionDao sectionDao, final StationService stationService) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationService = stationService;
    }

    public LineResponse saveLine(final LineRequest request) {
        final Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
        return LineResponse.of(persistLine);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findLineResponses() {
        final List<Line> persistLines = findLines();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Line> findLines() {
        return lineDao.findAll();
    }

    @Transactional(readOnly = true)
    public LineResponse findLineResponseById(final Long lineId) {
        final Line persistLine = findLineById(lineId);
        return LineResponse.of(persistLine);
    }

    @Transactional(readOnly = true)
    public Line findLineById(final Long id) {
        return lineDao.findById(id);
    }

    public LineResponse updateLine(final Long lineId, final LineRequest lineUpdateRequest) {
        final Line updateLine = new Line(lineId, lineUpdateRequest.getName(), lineUpdateRequest.getColor());
        lineDao.update(updateLine);
        return LineResponse.of(updateLine);
    }

    public void deleteLineById(final Long lineId) {
        lineDao.deleteById(lineId);
    }

    public SectionResponse enrollStation(final Long lineId, final StationEnrollRequest request) {
        final Line line = makeLineBy(lineId);
        final Station left = stationService.findById(request.getFromStation());
        final Station right = stationService.findById(request.getToStation());
        final Section section = new Section(left, right, new Distance(request.getDistance()));
        line.addSection(section);
        sectionDao.save(toEntities(line.getId(), line.getSections()));
        return new SectionResponse(lineId, left, right);
    }

    private List<SectionEntity> toEntities(final Long lineId, final List<Section> sections) {
        return sections.stream()
                .map(section -> new SectionEntity(
                        lineId,
                        section.getLeft().getName(),
                        section.getRight().getName(),
                        section.getDistance().getDistance())
                )
                .collect(Collectors.toList());
    }

    private Line makeLineBy(final Long lineId) {
        final Line line = findLineById(lineId);
        final List<Section> sections = findSections(lineId);
        return new Line(line.getId(), line.getName(), line.getColor(), sections);
    }

    private List<Section> findSections(final Long lineId) {
        final List<SectionEntity> sectionEntities = sectionDao.findById(lineId);
        final List<Station> stations = stationService.findStationsOf(sectionEntities);
        return sectionEntities.stream()
                .map(sectionEntity -> toSection(sectionEntity, stations))
                .collect(Collectors.toList());
    }

    private Section toSection(final SectionEntity sectionEntity, final List<Station> stations) {
        final Station leftStation = stations.stream().filter(station -> station.getName().equals(sectionEntity.getLeft())).findAny()
                .orElseThrow(() -> new IllegalArgumentException("Section 에 등록된 역이 존재하지 않습니다."));
        final Station rightStation = stations.stream().filter(station -> station.getName().equals(sectionEntity.getRight())).findAny()
                .orElseThrow(() -> new IllegalArgumentException("Section 에 등록된 역이 존재하지 않습니다."));
        return new Section(leftStation, rightStation, new Distance(sectionEntity.getDistance()));
    }

    public void deleteStation(final Long lineId, final Long stationId) {
        final Line line = makeLineBy(lineId);
        line.deleteStation(stationService.findById(stationId));
        sectionDao.save(toEntities(lineId, line.getSections()));
    }

    @Transactional(readOnly = true)
    public LineResponse findLineWithStationsById(final Long lineId) {
        final Line line = makeLineBy(lineId);
        return LineResponse.of(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllRouteMap() {
        final List<Line> linesWithStations = new ArrayList<>();
        for (final Line line : findLines()) {
            linesWithStations.add(makeLineBy(line.getId()));
        }
        return linesWithStations.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

}
