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
import subway.dao.entity.StationEntity;
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
        StationEntity startStation = findStationByName(sectionCreateRequest.getStartStationName());
        StationEntity endStation = findStationByName(sectionCreateRequest.getEndStationName());
        addSection(new SectionEntity(lineId, startStation.getId(), endStation.getId(),
                sectionCreateRequest.getDistance())); // TODO: 2023-05-15 Section 도메인으로 처리하는 방향 생각
    }

    private StationEntity findStationByName(String name) {
        if (stationDao.existsBy(name)) {
            return stationDao.findByName(name);
        }
        throw new StationNotFoundException();
    }

    private void addSection(SectionEntity section) {
        if (sectionDao.isEmptyByLineId(section.getLineId())) {
            sectionDao.insert(section);
            return;
        }
        boolean hasStartStation = sectionDao.isStationInLineById(section.getLineId(), section.getStartStationId());
        boolean hasEndStation = sectionDao.isStationInLineById(section.getLineId(), section.getEndStationId());
        validateHasStation(hasStartStation, hasEndStation);
        if (hasStartStation) {
            addEndSection(section);
        }
        if (hasEndStation) {
            addStartSection(section);
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

    private void addEndSection(SectionEntity sectionToAdd) {
        Long lineId = sectionToAdd.getLineId();
        sectionDao.findByStartStationIdAndLineId(sectionToAdd.getStartStationId(), lineId)
            .ifPresentOrElse(prevSection -> {
                int distance = prevSection.getDistance();
                int newDistance = sectionToAdd.getDistance();
                validateDistance(distance, newDistance);
                Long newSectionEndStationId = prevSection.getEndStationId();
                prevSection.updateEndStationId(sectionToAdd.getEndStationId());
                prevSection.updateDistance(newDistance);
                sectionDao.update(prevSection);
                sectionDao.insert(new SectionEntity(lineId, sectionToAdd.getEndStationId(), newSectionEndStationId, distance - newDistance));
            }, () -> sectionDao.insert(sectionToAdd));
    }

    private void validateDistance(int distance, int newDistance) {
        if (distance <= newDistance) {
            throw new IllegalDistanceException("새로운 구간의 길이는 기존 구간의 길이보다 작아야 합니다.");
        }
    }

    private void addStartSection(SectionEntity sectionToAdd) {
        Long lineId = sectionToAdd.getLineId();
        sectionDao.findByEndStationIdAndLineId(sectionToAdd.getEndStationId(), lineId)
                .ifPresentOrElse(prevSection -> {
                    int distance = prevSection.getDistance();
                    int newDistance = sectionToAdd.getDistance();
                    validateDistance(distance, newDistance);
                    Long newSectionStartStationId = prevSection.getStartStationId();
                    prevSection.updateStartStationId(sectionToAdd.getStartStationId());
                    prevSection.updateDistance(newDistance);
                    sectionDao.update(prevSection);
                    sectionDao.insert(new SectionEntity(lineId, newSectionStartStationId, sectionToAdd.getStartStationId(), distance - newDistance));
                }, () -> sectionDao.insert(sectionToAdd));
    }

    public void deleteSection(Long lineId, SectionDeleteRequest sectionDeleteRequest) {
        String stationName = sectionDeleteRequest.getStationName();
        StationEntity station = findStationByName(stationName);
        validateStationInLine(lineId, station);
        Optional<SectionEntity> startSection = sectionDao.findByEndStationIdAndLineId(station.getId(), lineId);
        Optional<SectionEntity> endSection = sectionDao.findByStartStationIdAndLineId(station.getId(), lineId);
        if (startSection.isPresent() && endSection.isPresent()) {
            mergeSection(startSection.get(), endSection.get());
            return;
        }
        endSection.ifPresent(sectionDao::deleteBy);
        startSection.ifPresent(sectionDao::deleteBy);
    }

    private void validateStationInLine(Long lineId, StationEntity station) {
        if (!sectionDao.isStationInLineById(lineId, station.getId())) {
            throw new StationNotFoundException();
        }
    }

    private void mergeSection(SectionEntity startSection, SectionEntity endSection) {
        sectionDao.deleteBy(startSection);
        endSection.updateDistance(startSection.getDistance() + endSection.getDistance());
        endSection.updateStartStationId(startSection.getStartStationId());
        sectionDao.update(endSection);
    }

    @Transactional(readOnly = true)
    public List<SectionResponse> findSectionsByLineId(Long lineId) {
        Sections sections = sectionDao.findAllByLineId(lineId).stream()
                .map(entity -> new Section(
                        Station.fromEntity(stationDao.findById(entity.getStartStationId())),
                        Station.fromEntity(stationDao.findById(entity.getEndStationId())),
                        entity.getDistance()))
                .collect(collectingAndThen(toList(), Sections::new));
        return sections.getSections().stream()
                .map(SectionResponse::from)
                .collect(toList());
    }
}
