package subway.application;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
public class SectionService {
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionService(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public void saveSection(Long lineId, SectionCreateRequest sectionCreateRequest) {
        Optional<Station> startStation = stationDao.findByName(sectionCreateRequest.getStartStationName());
        Optional<Station> endStation = stationDao.findByName(sectionCreateRequest.getEndStationName());
        if (startStation.isEmpty() || endStation.isEmpty()) {
            throw new StationNotFoundException();
        }
        addSection(lineId, new Section(startStation.get(), endStation.get(), sectionCreateRequest.getDistance()));
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

    private void validateHasStation(boolean hasStartStation, boolean hasEndStation) {
        if (hasStartStation && hasEndStation) {
            throw new IllegalSectionException("노선에 이미 해당 역이 존재합니다.");
        }
        if (!hasStartStation && !hasEndStation) {
            throw new IllegalSectionException("노선에 기준이 되는 역을 찾을 수 없습니다.");
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

    public void deleteSection(Long lineId, SectionDeleteRequest sectionDeleteRequest) {
        String stationName = sectionDeleteRequest.getStationName();
        validateStationInLine(lineId, stationName);
        Optional<SectionEntity> leftSection = sectionDao.findByEndStationNameAndLineId(stationName, lineId);
        Optional<SectionEntity> rightSection = sectionDao.findByStartStationNameAndLineId(stationName, lineId);
        if (leftSection.isPresent() && rightSection.isPresent()) {
            mergeSection(leftSection.get(), rightSection.get());
            return;
        }
        rightSection.ifPresent(sectionDao::deleteBy);
        leftSection.ifPresent(sectionDao::deleteBy);
    }

    private void validateStationInLine(Long lineId, String stationName) {
        if (!sectionDao.isStationInLine(lineId, stationName)) {
            throw new StationNotFoundException();
        }
    }

    private void mergeSection(SectionEntity leftSection, SectionEntity rightSection) {
        sectionDao.deleteBy(leftSection);
        sectionDao.update(rightSection.getId(), new Section(leftSection.getStartStation(), rightSection.getEndStation(),
                leftSection.getDistance() + rightSection.getDistance()));
    }

    @Transactional(readOnly = true)
    public List<SectionResponse> findSectionsByLineId(Long lineId) {
        Sections sections = sectionDao.findAllByLineId(lineId).stream()
                .map(Section::fromEntity)
                .collect(collectingAndThen(toList(), Sections::new));
        return sections.getSections().stream()
                .map(SectionResponse::from)
                .collect(toList());
    }
}
