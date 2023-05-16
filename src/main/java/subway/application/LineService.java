package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.LineRequest;
import subway.controller.dto.LineResponse;
import subway.controller.dto.SectionCreateRequest;
import subway.controller.dto.SectionDeleteRequest;
import subway.dao.entity.SectionEntity;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest request) {
        if (lineRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("동일한 이름을 가진 노선이 존재합니다.");
        }

        Line line = lineRepository.save(new Line(null, request.getName(), null));
        return LineResponse.of(line);
    }

    public LineResponse findLineResponseById(Long id) {
        Line line = lineRepository.findById(id);
        return LineResponse.of(line);
    }

    public List<LineResponse> findLineResponses() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional
    public void createSection(Long lineId, SectionCreateRequest sectionRequest) {
        Line line = lineRepository.findById(lineId);
        Station leftStation = stationRepository.findByName(sectionRequest.getLeftStationName());
        Station rightStation = stationRepository.findByName(sectionRequest.getRightStationName());

        int distance = sectionRequest.getDistance();
        if (line.getSections().isEmpty()) {
            insertSection(lineId, leftStation, rightStation, distance);
            return;
        }

        updateSection(line, leftStation, rightStation, distance);
    }


    private void updateSection(Line line, Station leftStation, Station rightStation, int distance) {
        if (line.hasStation(leftStation) == line.hasStation(rightStation)) {
            throw new IllegalArgumentException("잘못된 입력입니다.");
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
        SectionEntity sectionEntity = new SectionEntity(lineId, leftStation.getId(), rightStation.getId(),
                distance);
        lineRepository.saveSection(sectionEntity);
    }

    private void leftStationUpdate(Line line, Station leftStation, Station rightStation, int distance) {
        Section section = line.findSectionByLeftStation(leftStation);
        Station originLeft = section.getLeft();
        Station originRight = section.getRight();
        int originDistance = section.getDistance();

        if (distance >= originDistance) {
            throw new IllegalArgumentException("기존 거리보다 멀 수 없습니다.");
        }

        lineRepository.deleteSection(originLeft.getId(), originRight.getId());
        lineRepository.saveSection(
                new SectionEntity(line.getId(), leftStation.getId(), rightStation.getId(), distance));
        lineRepository.saveSection(new SectionEntity(line.getId(), rightStation.getId(), originRight.getId(),
                originDistance - distance));
    }

    private void rightStationUpdate(Line line, Station leftStation, Station rightStation, int distance) {
        Section section = line.findSectionByRightStation(rightStation);
        Station originLeft = section.getLeft();
        Station originRight = section.getRight();
        int originDistance = section.getDistance();

        if (distance >= originDistance) {
            throw new IllegalArgumentException("기존 거리보다 멀 수 없습니다.");
        }

        lineRepository.deleteSection(originLeft.getId(), originRight.getId());
        lineRepository.saveSection(new SectionEntity(line.getId(), originLeft.getId(), leftStation.getId(),
                originDistance - distance));
        lineRepository.saveSection(
                new SectionEntity(line.getId(), leftStation.getId(), rightStation.getId(), distance));
    }

    @Transactional
    public void deleteSection(Long lineId, SectionDeleteRequest deleteRequest) {
        Line line = lineRepository.findById(lineId);
        Station station = stationRepository.findByName(deleteRequest.getStationName());

        if (!line.hasStation(station)) {
            throw new IllegalArgumentException("노선에 해당 역이 존재하지 않습니다.");
        }

        if (line.hasOneSection()) {
            Section section = line.getSections().get(0);
            lineRepository.deleteSection(section.getLeft().getId(), section.getRight().getId());
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

        lineRepository.deleteSection(leftSection.getLeft().getId(), leftSection.getRight().getId());
        lineRepository.deleteSection(rightSection.getLeft().getId(), rightSection.getRight().getId());

        int newDistance = leftSection.getDistance() + rightSection.getDistance();
        lineRepository.saveSection(
                new SectionEntity(line.getId(), leftSection.getLeft().getId(), rightSection.getRight().getId(),
                        newDistance));
    }

    private void deleteLastSectionAtLeft(Line line, Station station) {
        Section section = line.findSectionByLeftStation(station);
        lineRepository.deleteSection(section.getLeft().getId(), section.getRight().getId());
    }

    private void deleteLastSectionAtRight(Line line, Station station) {
        Section section = line.findSectionByRightStation(station);
        lineRepository.deleteSection(section.getLeft().getId(), section.getRight().getId());
    }
}
