package subway.application.section;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.SectionEntity;
import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.dto.section.SectionCreateRequest;
import subway.dto.section.SectionDeleteRequest;
import subway.dto.section.SectionResponse;
import subway.exception.section.IllegalDistanceException;
import subway.exception.section.IllegalSectionException;
import subway.exception.station.StationNotFoundException;

@Service
@Transactional
public class SectionService {
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public SectionService(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public void saveSection(Long lineId, SectionCreateRequest request) {
        Optional<Station> upBoundStation = stationDao.findByName(request.getUpBoundStationName());
        Optional<Station> downBoundStation = stationDao.findByName(request.getDownBoundStationName());
        if (upBoundStation.isEmpty() || downBoundStation.isEmpty()) {
            throw new IllegalSectionException("추가하려는 역이 존재하지 않습니다.");
        }
        addSection(lineId,
                new Section(upBoundStation.get(), downBoundStation.get(), new Distance(request.getDistance())));
    }

    private void addSection(Long lineId, Section newSection) {
        if (sectionDao.isEmptyByLineId(lineId)) {
            sectionDao.insert(lineId, newSection);
            return;
        }
        boolean hasUpBoundStation = sectionDao.isStationInLine(lineId, newSection.getUpBoundStationName());
        boolean hasDownBoundStation = sectionDao.isStationInLine(lineId, newSection.getDownBoundStationName());
        validateHasStation(hasUpBoundStation, hasDownBoundStation);
        if (hasUpBoundStation) {
            addDownBoundSection(lineId, newSection);
        }
        if (hasDownBoundStation) {
            addUpBoundSection(lineId, newSection);
        }
    }

    private void validateHasStation(boolean hasUpBoundStation, boolean hasDownBoundStation) {
        if (hasUpBoundStation && hasDownBoundStation) {
            throw new IllegalSectionException("노선에 이미 해당 역이 존재합니다.");
        }
        if (!hasUpBoundStation && !hasDownBoundStation) {
            throw new IllegalSectionException("노선에 기준이 되는 역을 찾을 수 없습니다.");
        }
    }

    private void addDownBoundSection(Long lineId, Section newSection) {
        sectionDao.findByStartStationNameAndLineId(newSection.getUpBoundStationName(), lineId)
                .ifPresentOrElse(section -> {
                    int distance = section.getDistance();
                    int newDistance = newSection.getDistance();
                    validateDistance(distance, newDistance);
                    Station newStation = newSection.getDownBoundStation();
                    sectionDao.update(section.getId(),
                            new Section(section.getUpBoundStation(), newStation, new Distance(newDistance)));
                    sectionDao.insert(lineId, new Section(newStation, section.getDownBoundStation(),
                            new Distance(distance - newDistance)));
                }, () -> sectionDao.insert(lineId, newSection));
    }

    private void addUpBoundSection(Long lineId, Section newSection) {
        sectionDao.findByEndStationNameAndLineId(newSection.getDownBoundStationName(), lineId)
                .ifPresentOrElse(section -> {
                    int distance = section.getDistance();
                    int newDistance = newSection.getDistance();
                    validateDistance(distance, newDistance);
                    Station newStation = newSection.getUpBoundStation();
                    sectionDao.update(section.getId(),
                            new Section(newStation, section.getDownBoundStation(), new Distance(newDistance)));
                    sectionDao.insert(lineId,
                            new Section(section.getUpBoundStation(), newStation, new Distance(distance - newDistance)));
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
        sectionDao.update(rightSection.getId(),
                new Section(leftSection.getUpBoundStation(), rightSection.getDownBoundStation(),
                        new Distance(leftSection.getDistance() + rightSection.getDistance())));
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
