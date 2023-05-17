package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;
import subway.ui.dto.SectionRequest;

@Service
public class SectionCreateService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public SectionCreateService(
            final LineRepository lineRepository,
            final SectionRepository sectionRepository,
            final StationRepository stationRepository
    ) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public void createSection(SectionRequest sectionRequest) {
        Long lineId = sectionRequest.getLineId();
        Line line = findLine(lineId);

        Station leftStation = findStation(sectionRequest.getLeftStationName());
        Station rightStation = findStation(sectionRequest.getRightStationName());
        Distance distance = new Distance(sectionRequest.getDistance());
        Section section = new Section(leftStation, rightStation, distance);

        if (line.getSections().isEmpty()) {
            sectionRepository.save(lineId, section);
            return;
        }

        updateSection(line, leftStation, rightStation, sectionRequest.getDistance());
    }

    private Line findLine(Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("노선이 존재하지 않습니다."));
    }

    private Station findStation(String name) {
        return stationRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 역이 없습니다."));
    }

    private void updateSection(Line line, Station leftStation, Station rightStation, int distance) {
        validateStations(line, leftStation, rightStation);
        insertOrUpdateSection(line, leftStation, rightStation, distance);
    }

    private void validateStations(Line line, Station leftStation, Station rightStation) {
        if (line.hasStation(leftStation) == line.hasStation(rightStation)) {
            throw new IllegalArgumentException("잘못된 입력입니다.");
        }
    }

    private void insertOrUpdateSection(Line line, Station leftStation, Station rightStation, int distance) {
        if (line.hasStation(leftStation)) {
            if (!line.hasLeftStationInSection(leftStation)) {
                sectionRepository.save(line.getId(), new Section(leftStation, rightStation, new Distance(distance)));
                return;
            }

            leftStationUpdate(line, leftStation, rightStation, distance);
        }

        if (line.hasStation(rightStation)) {
            if (!line.hasRightStationInSection(rightStation)) {
                sectionRepository.save(line.getId(), new Section(leftStation, rightStation, new Distance(distance)));
                return;
            }

            rightStationUpdate(line, leftStation, rightStation, distance);
        }
    }

    private void leftStationUpdate(Line line, Station leftStation, Station rightStation, int distance) {
        Section section = line.findSectionByLeftStation(leftStation);
        Station originLeft = section.getLeft();
        Station originRight = section.getRight();
        int originDistance = section.getDistance();

        if (distance >= originDistance) {
            throw new IllegalArgumentException("기존 거리보다 멀 수 없습니다.");
        }

        sectionRepository.delete(originLeft.getId(), originRight.getId());
        sectionRepository.save(line.getId(), new Section(leftStation, rightStation, new Distance(distance)));
        sectionRepository.save(line.getId(),
                new Section(rightStation, originRight, new Distance(originDistance - distance)));
    }

    private void rightStationUpdate(Line line, Station leftStation, Station rightStation, int distance) {
        Section section = line.findSectionByRightStation(rightStation);
        Station originLeft = section.getLeft();
        Station originRight = section.getRight();
        int originDistance = section.getDistance();

        if (distance >= originDistance) {
            throw new IllegalArgumentException("기존 거리보다 멀 수 없습니다.");
        }

        sectionRepository.delete(originLeft.getId(), originRight.getId());
        sectionRepository.save(line.getId(),
                new Section(originLeft, leftStation, new Distance(originDistance - distance)));
        sectionRepository.save(line.getId(), new Section(leftStation, rightStation, new Distance(distance)));
    }
}
