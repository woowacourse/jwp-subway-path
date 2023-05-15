package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.SectionRequest;
import subway.dao.SectionDao;
import subway.dao.entity.SectionEntity;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@Service
public class SectionCreateService {

    private final SectionDao sectionDao;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public SectionCreateService(SectionDao sectionDao, LineRepository lineRepository,
                                StationRepository stationRepository) {
        this.sectionDao = sectionDao;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public void createSection(SectionRequest sectionRequest) {
        Long lineId = sectionRequest.getLineId();
        Line line = lineRepository.findById(lineId);

        Station leftStation = findStation(sectionRequest.getLeftStationName());
        Station rightStation = findStation(sectionRequest.getRightStationName());

        int distance = sectionRequest.getDistance();
        if (line.getSections().isEmpty()) {
            insertSection(lineId, leftStation, rightStation, distance);
            return;
        }

        updateSection(line, leftStation, rightStation, distance);
    }


    private Station findStation(String name) {
        return stationRepository.findByName(name);
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
        sectionDao.insert(sectionEntity);
    }

    private void leftStationUpdate(Line line, Station leftStation, Station rightStation, int distance) {
        Section section = line.findSectionByLeftStation(leftStation);
        Station originLeft = section.getLeft();
        Station originRight = section.getRight();
        int originDistance = section.getDistance();

        if (distance >= originDistance) {
            throw new IllegalArgumentException("기존 거리보다 멀 수 없습니다.");
        }

        sectionDao.deleteByStationId(originLeft.getId(), originRight.getId());
        sectionDao.insert(new SectionEntity(line.getId(), leftStation.getId(), rightStation.getId(), distance));
        sectionDao.insert(new SectionEntity(line.getId(), rightStation.getId(), originRight.getId(),
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

        sectionDao.deleteByStationId(originLeft.getId(), originRight.getId());
        sectionDao.insert(new SectionEntity(line.getId(), originLeft.getId(), leftStation.getId(),
                originDistance - distance));
        sectionDao.insert(new SectionEntity(line.getId(), leftStation.getId(), rightStation.getId(), distance));
    }
}
