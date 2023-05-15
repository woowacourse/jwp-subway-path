package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineStationDao;
import subway.dao.StationDao;
import subway.domain.line.Line;
import subway.domain.section.Distance;
import subway.domain.station.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.RegisterInnerStationRequest;
import subway.dto.RegisterLastStationRequest;
import subway.dto.RegisterStationsRequest;
import subway.dto.StationResponse;
import subway.entity.LineEntity;
import subway.entity.LineStationEntity;
import subway.entity.SectionEntity;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationDao stationDao;
    private final LineStationDao lineStationDao;
    private final SectionRepository sectionRepository;

    public LineService(LineRepository lineRepository, final StationDao stationDao, final LineStationDao lineStationDao,
                       final SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationDao = stationDao;
        this.lineStationDao = lineStationDao;
        this.sectionRepository = sectionRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        LineEntity persistLine = lineRepository.insert(new Line(request.getName(), request.getColor()));
        return LineResponse.of(persistLine);
    }

    public void registerInitStations(final String name, final RegisterStationsRequest registerStationsRequest) {
        Line line = lineRepository.findByName(name);

        Station leftStation = stationDao.findByName(registerStationsRequest.getLeftStationName()).orElseThrow(RuntimeException::new);
        Station rightStation = stationDao.findByName(registerStationsRequest.getRightStationName()).orElseThrow(RuntimeException::new);

        if (line.getStations().size() != 0) {
            throw new IllegalStateException("초기 등록할 때는 노선에 역이 하나도 없어야 합니다.");
        }

        lineRepository.saveInitStations(leftStation, rightStation, line, registerStationsRequest.getDistance());
    }

    public void registerLastStation(String name, RegisterLastStationRequest registerLastStationRequest) {
        Line line = lineRepository.findByName(name);
        Station baseStation = calculateAndValidateBaseStation(line.getUpBoundStation(), line.getDownBoundStation(),
                registerLastStationRequest.getBaseStation());
        Station inputStation = stationDao.findByName(registerLastStationRequest.getNewStationName()).orElseThrow(RuntimeException::new);
        insertLeftOrRightLastStation(baseStation, inputStation, line, new Distance(registerLastStationRequest.getDistance()));
        lineRepository.saveBoundStation(line, inputStation);
    }

    private Long insertLeftOrRightLastStation(Station baseStation, Station newStation, Line line, Distance distance) {
        if (baseStation.getName().equals(line.getUpBoundStation().getName())) {
            lineRepository.updateBoundStations(
                    new LineEntity(line.getId(), line.getName(), line.getColor(), newStation.getId(), line.getDownBoundStation().getId()));
            return sectionRepository.insert(new SectionEntity(newStation.getId(), baseStation.getId(), distance.getDistance(), line.getId())).getId();
        }
        if (baseStation.getName().equals(line.getDownBoundStation().getName())) {
            lineRepository.updateBoundStations(
                    new LineEntity(line.getId(), line.getName(), line.getColor(), line.getUpBoundStation().getId(), newStation.getId()));
            return sectionRepository.insert(new SectionEntity(baseStation.getId(), newStation.getId(), distance.getDistance(), line.getId())).getId();
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

    public void registerInnerStation(final String name, final RegisterInnerStationRequest registerInnerStationRequest) {
        Line line = lineRepository.findByName(name);
        List<Station> stations = lineRepository.findStations(
                registerInnerStationRequest.getLeftBaseStationName(),
                registerInnerStationRequest.getRightBaseStationName()
        );

        Station leftStation = stations.get(0);
        Station rightStation = stations.get(1);
        SectionEntity section = sectionRepository.findByLineIdAndLeftStationIdAndRightStationId(
                line.getId(), leftStation.getId(), rightStation.getId()).orElseThrow(RuntimeException::new);

        validateNewDistanceEqualBeforeDistance(section, registerInnerStationRequest);
        reCalculateInnerDistance(line.getId(), section, registerInnerStationRequest);
    }

    private void reCalculateInnerDistance(final Long lineId, final SectionEntity section,
                                          final RegisterInnerStationRequest registerInnerStationRequest) {
        Station newStation = stationDao.findByName(registerInnerStationRequest.getNewStationName()).orElseThrow(RuntimeException::new);
        lineStationDao.insert(new LineStationEntity(newStation.getId(), lineId));
        sectionRepository.deleteBySectionId(section.getId());
        sectionRepository.insertReCalculateSection(new SectionEntity(section.getLeftStationId(), newStation.getId(),
                registerInnerStationRequest.getLeftDistance(), lineId), new SectionEntity(newStation.getId(), section.getRightStationId(),
                registerInnerStationRequest.getRightDistance(), lineId));
    }

    private void validateNewDistanceEqualBeforeDistance(final SectionEntity section, final RegisterInnerStationRequest registerInnerStationRequest) {
        if (section.getDistance() == registerInnerStationRequest.getLeftDistance() + registerInnerStationRequest.getRightDistance()) {
            return;
        }
        throw new IllegalArgumentException("사이에 새로 추가하는 역과 기존 역간 거리가 잘못되었습니다.");
    }

    public void deleteStation(final String lineName, final String stationName) {
        Line line = lineRepository.findByName(lineName);
        if (line.isStationCount2()) {
            deleteLastStation(line);
            return;
        }
        Station station = stationDao.findByName(stationName).orElseThrow(RuntimeException::new);
        if (line.isBoundStation(station)) {
            deleteBoundStation(line, station);
            return;
        }
        lineStationDao.deleteByLineIdAndStationId(line.getId(), station.getId());
        SectionEntity leftSection = sectionRepository.findLeftByLineIdAndStationId(line.getId(), station.getId());
        SectionEntity rightSection = sectionRepository.findRightByLineIdAndStationId(line.getId(), station.getId());
        sectionRepository.deleteById(leftSection.getId());
        sectionRepository.deleteById(rightSection.getId());
        sectionRepository.insert(mixSection(leftSection, rightSection));
    }

    private SectionEntity mixSection(final SectionEntity leftSection, final SectionEntity rightSection) {
        if (leftSection.getLineId() != rightSection.getLineId()) {
            throw new IllegalArgumentException("같은 노선의 구역이 아닙니다.");
        }
        if ((leftSection.getRightStationId() != rightSection.getLeftStationId()) && (rightSection.getRightStationId() != leftSection.getLeftStationId())) {
            throw new IllegalArgumentException("이어진 구역이 아닙니다.");
        }

        return new SectionEntity(leftSection.getLeftStationId(), rightSection.getRightStationId(),
                leftSection.getDistance() + rightSection.getDistance(), leftSection.getLineId());
    }

    private void deleteBoundStation(final Line line, final Station station) {
        lineStationDao.deleteByLineIdAndStationId(line.getId(), station.getId());
        SectionEntity section = sectionRepository.findByStationId(station.getId()).orElseThrow(RuntimeException::new);
        sectionRepository.deleteById(section.getId());
        if (line.isUpBoundStation(station)) {
            lineRepository.updateBoundStations(new LineEntity(line.getId(), line.getName(), line.getColor(),
                    section.getRightStationId(), line.getDownBoundStation().getId()));
            return;
        }
        lineRepository.updateBoundStations(new LineEntity(line.getId(), line.getName(), line.getColor(),
                line.getUpBoundStation().getId(), section.getLeftStationId()));
    }

    private void deleteLastStation(final Line line) {
        lineStationDao.deleteByLineId(line.getId());
        sectionRepository.deleteAllSectionsByLine(line);
        lineRepository.updateBoundStations(new LineEntity(line.getId(), line.getName(), line.getColor(), null, null));
    }

    public LineResponse findLineResponseByName(final String name) {
        Line line = lineRepository.findByName(name);
        List<StationResponse> stationResponses = line.getStations().getStations().stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toUnmodifiableList());

        return new LineResponse(line.getId(), line.getName(), line.getColor(), stationResponses);
    }

    public List<LineResponse> findAll() {
        List<LineEntity> lineEntities = lineRepository.findAll();
        return lineEntities.stream()
                .map(lineEntity -> findLineResponseByName(lineEntity.getName()))
                .collect(Collectors.toUnmodifiableList());
    }
}
