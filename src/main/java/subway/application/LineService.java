package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dao.LineSectionDao;
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
import subway.entity.LineEntity;
import subway.entity.LineSectionEntity;
import subway.entity.LineStationEntity;
import subway.entity.SectionEntity;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;

@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationDao stationDao;
    private final LineStationDao lineStationDao;
    private final SectionRepository sectionRepository;
    private final LineSectionDao lineSectionDao;

    public LineService(LineRepository lineRepository, final StationDao stationDao, final LineStationDao lineStationDao,
        final SectionRepository sectionRepository, final LineSectionDao lineSectionDao) {
        this.lineRepository = lineRepository;
        this.stationDao = stationDao;
        this.lineStationDao = lineStationDao;
        this.sectionRepository = sectionRepository;
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
        SectionEntity sectionEntity = sectionRepository.insert(
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
            return sectionRepository.insert(new SectionEntity(newStation.getId(), baseStation.getId(), distance.getDistance())).getId();
        }
        if (baseStation.getName().equals(line.getDownBoundStation().getName())) {
            lineRepository.updateBoundStations(
                new LineEntity(line.getId(), line.getName(), line.getColor(), line.getUpBoundStation().getId(), newStation.getId()));
            return sectionRepository.insert(new SectionEntity(baseStation.getId(), newStation.getId(), distance.getDistance())).getId();
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

        Station leftStation = stationDao.findByName(registerInnerStationRequest.getLeftBaseStationName()).orElseThrow(RuntimeException::new);
        Station rightStation = stationDao.findByName(registerInnerStationRequest.getRightBaseStationName()).orElseThrow(RuntimeException::new);
        SectionEntity section = sectionRepository.findByLineIdAndLeftStationIdAndRightStationId(
            line.getId(), leftStation.getId(), rightStation.getId()).orElseThrow(RuntimeException::new);

        validateNewDistanceEqualBeforeDistance(section, registerInnerStationRequest);
        reCalculateInnerDistance(line.getId(), section, registerInnerStationRequest);
    }

    private void reCalculateInnerDistance(final Long lineId, final SectionEntity section,
        final RegisterInnerStationRequest registerInnerStationRequest) {
        Station newStation = stationDao.findByName(registerInnerStationRequest.getNewStationName()).orElseThrow(RuntimeException::new);
        lineStationDao.insert(new LineStationEntity(newStation.getId(), lineId));
        sectionRepository.deleteSectionAndLineSectionBySectionId(section.getId());
        sectionRepository.insertReCalculateSection(lineId, new SectionEntity(section.getLeftStationId(), newStation.getId(),
            registerInnerStationRequest.getLeftDistance()), new SectionEntity(newStation.getId(), section.getRightStationId(),
            registerInnerStationRequest.getRightDistance()));
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
        List<SectionEntity> sectionEntities = sectionRepository.findByLineIdAndStationId(line.getId(), station.getId());
        for (SectionEntity section : sectionEntities) {
            lineSectionDao.deleteBySectionId(section.getId());
            sectionRepository.deleteById(section.getId());
        }
        SectionEntity insertedSection = sectionRepository.insert(mixSection(sectionEntities, station.getId()));
        lineSectionDao.insert(new LineSectionEntity(line.getId(), insertedSection.getId()));
    }

    private SectionEntity mixSection(final List<SectionEntity> sectionEntities, final Long stationId) {
        SectionEntity leftSection = sectionEntities.get(0);
        SectionEntity rightSection = sectionEntities.get(1);
        if (leftSection.getRightStationId() != stationId) {
            leftSection = sectionEntities.get(1);
            rightSection = sectionEntities.get(0);
        }

        return new SectionEntity(leftSection.getLeftStationId(), rightSection.getRightStationId(),
            leftSection.getDistance() + rightSection.getDistance());
    }

    private void deleteBoundStation(final Line line, final Station station) {
        lineStationDao.deleteByLineIdAndStationId(line.getId(), station.getId());
        SectionEntity section = sectionRepository.findByStationId(station.getId()).orElseThrow(RuntimeException::new);
        lineSectionDao.deleteBySectionId(section.getId());
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
        List<LineSectionEntity> lineSectionEntities = lineSectionDao.findByLineId(line.getId());
        lineSectionDao.deleteByLineId(line.getId());
        sectionRepository.deleteById(lineSectionEntities.get(0).getSectionId());
        lineRepository.updateBoundStations(new LineEntity(line.getId(), line.getName(), line.getColor(), null, null));
    }
}
