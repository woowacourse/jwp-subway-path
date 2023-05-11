package subway.application;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.SectionDto;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.dto.LineDto;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.ui.dto.SectionRequest;

@Service
public class SectionCreateService {

    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final StationDao stationDao;

    public SectionCreateService(SectionDao sectionDao, LineDao lineDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    @Transactional
    public void createSection(SectionRequest sectionRequest) {
        Long lineId = sectionRequest.getLineId();
        LineDto foundLine = findLine(lineId);
        Line line = makeLine(lineId, foundLine);

        Station leftStation = findStation(sectionRequest.getLeftStationName());
        Station rightStation = findStation(sectionRequest.getRightStationName());

        int distance = sectionRequest.getDistance();
        if (line.getSections().isEmpty()) {
            insertSection(lineId, leftStation, rightStation, distance);
            return;
        }

        updateSection(line, leftStation, rightStation, distance);
    }

    private LineDto findLine(Long lineId) {
        return lineDao.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("노선이 존재하지 않습니다."));
    }

    private Line makeLine(Long lineId, LineDto foundLine) {
        List<SectionDto> sectionDtos = sectionDao.findByLineId(lineId);
        LinkedList<Section> sections = sectionDtos.stream()
                .map(sectionDto -> new Section(
                                stationDao.findById(sectionDto.getLeftStationId()),
                                stationDao.findById(sectionDto.getRightStationId()),
                                new Distance(sectionDto.getDistance())
                        )
                ).collect(Collectors.toCollection(LinkedList::new));

        return new Line(foundLine.getId(), foundLine.getName(), sections);
    }

    private Station findStation(String name) {
        return stationDao.findByName(name)
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
        SectionDto sectionDto = new SectionDto(lineId, leftStation.getId(), rightStation.getId(),
                distance);
        sectionDao.insert(sectionDto);
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
        sectionDao.insert(new SectionDto(line.getId(), leftStation.getId(), rightStation.getId(), distance));
        sectionDao.insert(new SectionDto(line.getId(), rightStation.getId(), originRight.getId(),
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
        sectionDao.insert(new SectionDto(line.getId(), originLeft.getId(), leftStation.getId(),
                originDistance - distance));
        sectionDao.insert(new SectionDto(line.getId(), leftStation.getId(), rightStation.getId(), distance));
    }
}
