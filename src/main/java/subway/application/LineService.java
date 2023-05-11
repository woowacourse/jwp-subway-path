package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.line.Line;
import subway.domain.station.StationConnections;
import subway.dto.LineFindResponse;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.entity.LineEntity;
import subway.entity.SectionStationJoinEntity;

import java.util.List;

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
        Long persistLine = lineDao.insert(null);
        return null;
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

    public LineFindResponse findStationNamesByLineId(Long lineId) {
        LineEntity lineEntity = lineDao.findById(lineId);
        List<SectionStationJoinEntity> findSectionStationEntities = sectionDao.findSectionStationByLineId(lineId);
        StationConnections stationConnections = StationConnections.fromEntities(findSectionStationEntities);
        return new LineFindResponse(lineEntity.getName(), stationConnections.getSortedStationNames());
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }

}
