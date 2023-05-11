package subway.application;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.SectionEntity;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.section.SectionCreateRequest;
import subway.dto.section.SectionDeleteRequest;
import subway.dto.section.SectionResponse;
import subway.exception.IllegalDistanceException;
import subway.exception.IllegalSectionException;
import subway.exception.StationNotFoundException;

@Service
public class SectionService {
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionService(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public void saveSection(Long lineId, SectionCreateRequest sectionCreateRequest) {
        String startStationName = sectionCreateRequest.getStartStationName();
        String endStationName = sectionCreateRequest.getEndStationName();
        validateStation(startStationName, endStationName);
        addSection(lineId, new Section(new Station(startStationName), new Station(endStationName),
                sectionCreateRequest.getDistance()));
    }

    private void validateStation(String startStationName, String endStationName) {
        if (!stationDao.existsBy(startStationName) || !stationDao.existsBy(endStationName)) {
            throw new StationNotFoundException();
        }
    }

    private void addSection(Long lineId, Section newSection) {
        if (sectionDao.isEmptyByLineId(lineId)) {
            sectionDao.insert(lineId, newSection);
            return;
        }
        boolean hasStartStation = sectionDao.isStationInLine(lineId, newSection.getStartStationName());
        boolean hasEndStation = sectionDao.isStationInLine(lineId, newSection.getEndStationName());
        validateHasStation(hasStartStation, hasEndStation);
        if (hasStartStation) {
            addEndSection(lineId, newSection);
        }
        if (hasEndStation) {
            addStartSection(lineId, newSection);
        }
    }

    private void addEndSection(Long lineId, Section newSection) {
        sectionDao.findByStartStationNameAndLineId(newSection.getStartStationName(), lineId)
            .ifPresentOrElse(section -> {
                int distance = section.getDistance();
                int newDistance = newSection.getDistance();
                validateDistance(distance, newDistance);
                Station newStation = newSection.getEndStation();
                sectionDao.update(section.getId(), new Section(section.getStartStation(), newStation, newDistance));
                sectionDao.insert(lineId, new Section(newStation, section.getEndStation(), distance - newDistance));
            }, () -> sectionDao.insert(lineId, newSection));
    }

    private void addStartSection(Long lineId, Section newSection) {
        sectionDao.findByEndStationNameAndLineId(newSection.getEndStationName(), lineId)
                .ifPresentOrElse(section -> {
                    int distance = section.getDistance();
                    int newDistance = newSection.getDistance();
                    validateDistance(distance, newDistance);
                    Station newStation = newSection.getStartStation();
                    sectionDao.update(section.getId(), new Section(newStation, section.getEndStation(), newDistance));
                    sectionDao.insert(lineId, new Section(section.getStartStation(), newStation, distance - newDistance));
                }, () -> sectionDao.insert(lineId, newSection));
    }

    private void validateDistance(int distance, int newDistance) {
        if (distance <= newDistance) {
            throw new IllegalDistanceException("새로운 구간의 길이는 기존 구간의 길이보다 작아야 합니다.");
        }
    }

    private void validateHasStation(boolean hasStartStation, boolean hasEndStation) {
        if (hasStartStation && hasEndStation) {
            throw new IllegalSectionException("이미 노선에 추가할 역이 존재합니다.");
        }
        if (!hasStartStation && !hasEndStation) {
            throw new IllegalSectionException("노선에 기준이 되는 역을 찾을 수 없습니다.");
        }
    }

    public void deleteSection(Long lineId, SectionDeleteRequest sectionDeleteRequest) {
        String stationName = sectionDeleteRequest.getStationName();
        validateStationInLine(lineId, stationName);
        Optional<SectionEntity> startSection = sectionDao.findByEndStationNameAndLineId(stationName, lineId);
        Optional<SectionEntity> endSection = sectionDao.findByStartStationNameAndLineId(stationName, lineId);
        if (startSection.isPresent() && endSection.isPresent()) {
            mergeSection(startSection.get(), endSection.get());
            return;
        }
        endSection.ifPresent(sectionDao::deleteBy);
        startSection.ifPresent(sectionDao::deleteBy);
    }

    private void validateStationInLine(Long lineId, String stationName) {
        if (!sectionDao.isStationInLine(lineId, stationName)) {
            throw new StationNotFoundException();
        }
    }

    private void mergeSection(SectionEntity startSection, SectionEntity endSection) {
        sectionDao.deleteBy(startSection);
        sectionDao.update(endSection.getId(), new Section(startSection.getStartStation(), endSection.getEndStation(),
                startSection.getDistance() + endSection.getDistance()));
    }

    public List<SectionResponse> findSectionsByLineId(Long lineId) {
        Sections sections = sectionDao.findAllByLineId(lineId).stream()
                .map(Section::fromEntity)
                .collect(collectingAndThen(toList(), Sections::new));
        return sections.getSections().stream()
                .map(SectionResponse::from)
                .collect(toList());
    }
}
