package subway.domain.line.service;

import org.springframework.stereotype.Service;
import subway.domain.line.dao.LineDao;
import subway.domain.line.domain.Line;
import subway.domain.line.domain.SectionLocator;
import subway.domain.line.domain.SectionRouter;
import subway.domain.line.dto.LineRequest;
import subway.domain.line.dto.StationRegisterRequest;
import subway.domain.line.entity.LineEntity;
import subway.domain.line.entity.SectionEntity;
import subway.domain.line.entity.StationEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final StationService stationService;
    private final SectionService sectionService;
    private final LineDao lineDao;

    public LineService(final StationService stationService, final SectionService sectionService, final LineDao lineDao) {
        this.stationService = stationService;
        this.sectionService = sectionService;
        this.lineDao = lineDao;
    }

    public List<Line> findAllLine() {
        Optional<List<LineEntity>> findLines = lineDao.findAll();
        List<LineEntity> lines = findLines.get();

        return lines.stream()
                .map(LineEntity::getId)
                .map(this::findLineById)
                .collect(Collectors.toList());
    }

    public Line findLineById(Long id) {
        List<SectionEntity> sectionDetails = sectionService.findByLineId(id);

        SectionLocator sectionLocator = SectionLocator.of(sectionDetails);
        SectionRouter sectionRouter = SectionRouter.of(sectionDetails);

        return createLine(id, sectionLocator, sectionRouter);
    }

    private Line createLine(Long lineId, SectionLocator sectionLocator, SectionRouter sectionRouter) {
        Long startStation = sectionLocator.findStartStation();
        Long endStation = sectionLocator.findEndStation();

        List<Long> shortestPath = sectionRouter.findShortestPath(startStation, endStation);
        Optional<LineEntity> findLine = lineDao.findById(lineId);

        if (findLine.isEmpty()) {
            throw new IllegalArgumentException("해당 노선이 존재하지 않습니다.");
        }

        List<StationEntity> stations = stationService.findStationsByIds(shortestPath);
        return new Line(findLine.get().getId(), findLine.get().getName(), findLine.get().getColor(), stations);
    }

    public LineEntity saveLine(final LineRequest request) {
        Optional<LineEntity> line = lineDao.findByName(request.getName());
        if (line.isPresent()) {
            throw new IllegalArgumentException("노선 이름이 이미 존재합니다. 유일한 노선 이름을 사용해주세요.");
        }
        return lineDao.insert(new LineEntity(request.getName(), request.getColor()));
    }

    public void updateLine(final Long id, final LineRequest lineUpdateRequest) {
        Optional<LineEntity> findLine = lineDao.findById(id);
        if (findLine.isEmpty()) {
            throw new IllegalArgumentException("해당 노선이 존재하지 않습니다.");
        }
        lineDao.update(new LineEntity(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(final Long id) {
        Optional<LineEntity> findLine = lineDao.findById(id);
        if (findLine.isEmpty()) {
            throw new IllegalArgumentException("해당 노선이 존재하지 않습니다.");
        }
        lineDao.deleteById(id);
    }

    public List<SectionEntity> addStation(Long lineId, StationRegisterRequest request) {
        return sectionService.createSection(lineId, request);
    }

    public void deleteStation(final Long lineId, final Long stationId) {
        sectionService.deleteSection(lineId, stationId);
    }
}
