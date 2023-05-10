package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.LineSectionDao;
import subway.dao.LineStationDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.Stations;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.RegisterStationsRequest;
import subway.entity.LineSectionEntity;
import subway.entity.LineStationEntity;
import subway.entity.SectionEntity;

@Service
public class LineService {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final LineStationDao lineStationDao;
    private final SectionDao sectionDao;
    private final LineSectionDao lineSectionDao;

    public LineService(LineDao lineDao, final StationDao stationDao, final LineStationDao lineStationDao, final SectionDao sectionDao,
        final LineSectionDao lineSectionDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.lineStationDao = lineStationDao;
        this.sectionDao = sectionDao;
        this.lineSectionDao = lineSectionDao;
    }

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineDao.insert(new Line(request.getName(), request.getColor()));
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
        //lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }


    public void registerInitStations(final String name, final RegisterStationsRequest registerStationsRequest) {
        Line line = lineDao.findByName(name).orElseThrow(RuntimeException::new);

        Station leftStation = stationDao.findByName(registerStationsRequest.getLeftStationName()).orElseThrow(RuntimeException::new);
        Station rightStation = stationDao.findByName(registerStationsRequest.getRightStationName()).orElseThrow(RuntimeException::new);

        List<LineStationEntity> lineStationEntities = lineStationDao.findByLineId(line.getId());
        if (lineStationEntities.size() != 0) {
            throw new IllegalStateException("초기화 할 때는 노선에 역이 하나도 없어여 합니다.");
        }

        lineStationDao.insert(new LineStationEntity(leftStation.getId(), line.getId()));
        lineStationDao.insert(new LineStationEntity(rightStation.getId(), line.getId()));
        SectionEntity sectionEntity = sectionDao.insert(new SectionEntity(leftStation.getId(), rightStation.getId(), registerStationsRequest.getDistance()));
        lineSectionDao.insert(new LineSectionEntity(line.getId(), sectionEntity.getId()));
        Line newLine = new Line(
            line.getId(),
            line.getName(),
            line.getColor(),
            new Stations(List.of(leftStation, rightStation)),
            new Sections(List.of(new Section(sectionEntity.getId(), leftStation, rightStation, registerStationsRequest.getDistance()))));
        lineDao.update(newLine);
    }
}
