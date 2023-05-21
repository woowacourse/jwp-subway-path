package subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.LineRequest;
import subway.controller.dto.SectionCreateRequest;
import subway.controller.dto.SectionDeleteRequest;
import subway.dao.entity.SectionEntity;
import subway.domain.line.Line;
import subway.domain.line.Section;
import subway.domain.line.Station;
import subway.exception.InvalidDistanceException;
import subway.exception.LineNameException;
import subway.exception.LineStationAdditionException;
import subway.exception.StationNotFoundException;
import subway.repository.LineRepository;

@Transactional(readOnly = true)
@Service
public class LineService {

    private final StationService stationService;
    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository, StationService stationService) {
        this.lineRepository = lineRepository;
        this.stationService = stationService;
    }

    @Transactional
    public Line saveLine(LineRequest request) {
        if (lineRepository.existsByName(request.getName())) {
            throw new LineNameException("동일한 이름을 가진 노선이 존재합니다.");
        }

        return lineRepository.save(new Line(null, request.getName(), null));
    }

    public Line findLineResponseById(Long id) {
        return lineRepository.findById(id);
    }

    public List<Line> findLineResponses() {
        return lineRepository.findAll();
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void createSection(Long lineId, SectionCreateRequest sectionRequest) {
        Line line = lineRepository.findById(lineId);
        Station leftStation = stationService.findByName(sectionRequest.getLeftStationName());
        Station rightStation = stationService.findByName(sectionRequest.getRightStationName());

        int distance = sectionRequest.getDistance();
        if (line.getSections().isEmpty()) {
            insertSection(lineId, leftStation, rightStation, distance);
            return;
        }

        updateSection(line, leftStation, rightStation, distance);
    }


    private void updateSection(Line line, Station leftStation, Station rightStation, int distance) {
        if (line.hasStation(leftStation) == line.hasStation(rightStation)) {
            throw new LineStationAdditionException("해당 역을 추가할 수 없습니다.");
        }

        insertOrUpdateSection(line, leftStation, rightStation, distance);
    }

    private void insertOrUpdateSection(Line line, Station leftStation, Station rightStation, int distance) {
        if (line.hasStation(leftStation)) {
            if (!line.hasLeftStationInSection(leftStation)) {
                insertSection(line.getId(), leftStation, rightStation, distance);
                return;
            }

            leftStationUpdate(line, leftStation, rightStation, distance);
        }

        if (line.hasStation(rightStation)) {
            if (!line.hasRightStationInSection(rightStation)) {
                insertSection(line.getId(), leftStation, rightStation, distance);
                return;
            }

            rightStationUpdate(line, leftStation, rightStation, distance);
        }
    }

    private void insertSection(Long lineId, Station leftStation, Station rightStation, int distance) {
        SectionEntity sectionEntity = new SectionEntity(null, lineId, leftStation.getId(), rightStation.getId(),
                distance);
        lineRepository.saveSection(sectionEntity);
    }

    private void leftStationUpdate(Line line, Station leftStation, Station rightStation, int distance) {
        Section section = line.findSectionByLeftStation(leftStation);
        Station originRight = section.getRight();
        int originDistance = section.getDistance();

        if (distance >= originDistance) {
            throw new InvalidDistanceException("기존 거리보다 멀 수 없습니다.");
        }

        lineRepository.deleteSection(section.getId());
        lineRepository.saveSection(
                new SectionEntity(null, line.getId(), leftStation.getId(), rightStation.getId(), distance));
        lineRepository.saveSection(new SectionEntity(null, line.getId(), rightStation.getId(), originRight.getId(),
                originDistance - distance));
    }

    private void rightStationUpdate(Line line, Station leftStation, Station rightStation, int distance) {
        Section section = line.findSectionByRightStation(rightStation);
        Station originLeft = section.getLeft();
        int originDistance = section.getDistance();

        if (distance >= originDistance) {
            throw new InvalidDistanceException("기존 거리보다 멀 수 없습니다.");
        }

        lineRepository.deleteSection(section.getId());
        lineRepository.saveSection(new SectionEntity(null, line.getId(), originLeft.getId(), leftStation.getId(),
                originDistance - distance));
        lineRepository.saveSection(
                new SectionEntity(null, line.getId(), leftStation.getId(), rightStation.getId(), distance));
    }

    @Transactional
    public void deleteSection(Long lineId, SectionDeleteRequest deleteRequest) {
        Line line = lineRepository.findById(lineId);
        Station station = stationService.findByName(deleteRequest.getStationName());

        if (!line.hasStation(station)) {
            throw new StationNotFoundException("노선에 해당 역이 존재하지 않습니다.");
        }

        if (line.hasOneSection()) {
            Section section = line.getSections().get(0);
            lineRepository.deleteSection(section.getId());
            return;
        }

        processSectionDeletion(line, station);
    }

    private void processSectionDeletion(Line line, Station station) {
        if (line.hasLeftStationInSection(station) && line.hasRightStationInSection(station)) {
            deleteMiddleSection(line, station);
            return;
        }

        if (line.isLastStationAtLeft(station)) {
            deleteLastSectionAtLeft(line, station);
        }

        if (line.isLastStationAtRight(station)) {
            deleteLastSectionAtRight(line, station);
        }
    }

    private void deleteMiddleSection(Line line, Station station) {
        Section leftSection = line.findSectionByRightStation(station);
        Section rightSection = line.findSectionByLeftStation(station);

        lineRepository.deleteSection(leftSection.getId());
        lineRepository.deleteSection(rightSection.getId());

        int newDistance = leftSection.getDistance() + rightSection.getDistance();
        lineRepository.saveSection(
                new SectionEntity(null, line.getId(), leftSection.getLeft().getId(), rightSection.getRight().getId(),
                        newDistance));
    }

    private void deleteLastSectionAtLeft(Line line, Station station) {
        Section section = line.findSectionByLeftStation(station);
        lineRepository.deleteSection(section.getId());
    }

    private void deleteLastSectionAtRight(Line line, Station station) {
        Section section = line.findSectionByRightStation(station);
        lineRepository.deleteSection(section.getId());
    }
}
