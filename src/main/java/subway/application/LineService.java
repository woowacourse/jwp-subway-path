package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dao.LineSectionDao;
import subway.dao.LineStationDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.line.Line;
import subway.domain.section.Distance;
import subway.domain.station.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.RegisterLastStationRequest;
import subway.dto.RegisterStationsRequest;
import subway.entity.LineEntity;
import subway.entity.LineSectionEntity;
import subway.entity.LineStationEntity;
import subway.entity.SectionEntity;
import subway.repository.LineRepository;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationDao stationDao;
    private final LineStationDao lineStationDao;
    private final SectionDao sectionDao;
    private final LineSectionDao lineSectionDao;

    public LineService(LineRepository lineRepository, final StationDao stationDao, final LineStationDao lineStationDao, final SectionDao sectionDao,
        final LineSectionDao lineSectionDao) {
        this.lineRepository = lineRepository;
        this.stationDao = stationDao;
        this.lineStationDao = lineStationDao;
        this.sectionDao = sectionDao;
        this.lineSectionDao = lineSectionDao;
    }

    public LineResponse saveLine(LineRequest request) {
        LineEntity persistLine = lineRepository.insert(new Line(request.getName(), request.getColor()));
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> findLineResponses() {
        List<LineEntity> persistLines = findLines();
        return persistLines.stream()
            .map(LineResponse::of)
            .collect(Collectors.toList());
    }

    public List<LineEntity> findLines() {
        return lineRepository.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        LineEntity persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    public LineEntity findLineById(Long id) {
        return lineRepository.findById(id);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        //lineDao.update(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }


    public void registerInitStations(final String name, final RegisterStationsRequest registerStationsRequest) {
        LineEntity line = lineRepository.findEntityByName(name).orElseThrow(RuntimeException::new);

        Station leftStation = stationDao.findByName(registerStationsRequest.getLeftStationName()).orElseThrow(RuntimeException::new);
        Station rightStation = stationDao.findByName(registerStationsRequest.getRightStationName()).orElseThrow(RuntimeException::new);

        List<LineStationEntity> lineStationEntities = lineStationDao.findByLineId(line.getId());
        if (lineStationEntities.size() != 0) {
            throw new IllegalStateException("초기화 할 때는 노선에 역이 하나도 없어여 합니다.");
        }

        lineStationDao.insert(new LineStationEntity(leftStation.getId(), line.getId()));
        lineStationDao.insert(new LineStationEntity(rightStation.getId(), line.getId()));
        SectionEntity sectionEntity = sectionDao.insert(
            new SectionEntity(leftStation.getId(), rightStation.getId(), registerStationsRequest.getDistance()));
        lineSectionDao.insert(new LineSectionEntity(line.getId(), sectionEntity.getId()));
        LineEntity newLine = new LineEntity(
            line.getId(),
            line.getName(),
            line.getColor(),
            leftStation.getId(),
            rightStation.getId()
        );
        lineRepository.updateBoundStations(newLine);
    }

    public void registerLastStation(String name, RegisterLastStationRequest registerLastStationRequest) {
        Line line = lineRepository.findByName(name);
        Station baseStation = calculateAndValidateBaseStation(line.getUpBoundStation(), line.getDownBoundStation(),
            registerLastStationRequest.getBaseStation());
        Station inputStation = stationDao.findByName(registerLastStationRequest.getNewStationName()).orElseThrow(RuntimeException::new);
        Long sectionId = insertLeftOrRightLastStation(baseStation, inputStation, line, new Distance(registerLastStationRequest.getDistance()));
        lineSectionDao.insert(new LineSectionEntity(line.getId(), sectionId));
        lineStationDao.insert(new LineStationEntity(inputStation.getId(), line.getId()));
    }

    private Long insertLeftOrRightLastStation(Station baseStation, Station newStation, Line line, Distance distance) {
        if (baseStation.getName().equals(line.getUpBoundStation().getName())) {
            lineRepository.updateBoundStations(
                new LineEntity(line.getId(), line.getName(), line.getColor(), newStation.getId(), line.getDownBoundStation().getId()));
            return sectionDao.insert(new SectionEntity(newStation.getId(), baseStation.getId(), distance.getDistance())).getId();
        }
        if (baseStation.getName().equals(line.getDownBoundStation().getName())) {
            lineRepository.updateBoundStations(
                new LineEntity(line.getId(), line.getName(), line.getColor(), line.getUpBoundStation().getId(), newStation.getId()));
            return sectionDao.insert(new SectionEntity(baseStation.getId(), newStation.getId(), distance.getDistance())).getId();
        }
        throw new IllegalStateException("기준점이 종점이 아닙니다.");
    }

    private Station calculateAndValidateBaseStation(Station upBoundStation, Station downBoundStation, String baseStationName) {
        if (upBoundStation.getName().equals(baseStationName)) {
            return upBoundStation;
        }
        if (downBoundStation.getName().equals(baseStationName)) {
            return downBoundStation;
        }

        throw new IllegalArgumentException("기준점이 종점이 아닙니다.");
    }
}
