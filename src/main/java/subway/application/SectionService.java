package subway.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.domain.Section;
import subway.domain.SectionRepository;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.dto.section.SectionCreateRequest;
import subway.dto.section.SectionDeleteRequest;
import subway.dto.section.SectionResponse;
import subway.exception.IllegalDistanceException;
import subway.exception.IllegalSectionException;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;

@Service
@Transactional
public class SectionService {
    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public SectionService(SectionDao sectionDao, StationDao stationDao, LineDao lineDao, LineRepository lineRepository,
                          StationRepository stationRepository, SectionRepository sectionRepository) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public void saveSection(Long lineId, SectionCreateRequest sectionCreateRequest) {
        Station startStation = stationRepository.findByName(sectionCreateRequest.getStartStationName());
        Station endStation = stationRepository.findByName(sectionCreateRequest.getEndStationName());

        Line line = lineRepository.findById(lineId);
        addSection(line, new Section(startStation, endStation, sectionCreateRequest.getDistance()));
    }

    private void addSection(Line line, Section sectionToAdd) {
        if (line.isEmpty()) {
            sectionRepository.save(line.getId(), sectionToAdd);
            return;
        }

        boolean isStartStationAddRequest = line.hasStationInLine(sectionToAdd.getEndStation());
        boolean isEndStationAddRequest = line.hasStationInLine(sectionToAdd.getStartStation());
        validateHasStation(isStartStationAddRequest, isEndStationAddRequest);

        if (isStartStationAddRequest) {
            addStartStation(line, sectionToAdd);
        }
        if (isEndStationAddRequest) {
            addEndStation(line, sectionToAdd);
        }
    }

    private LineEntity findLineById(Long id) {
        Optional<LineEntity> lineEntity = lineDao.findById(id);
        if (lineEntity.isPresent()) {
            return lineEntity.get();
        }
        throw new LineNotFoundException();
    }

    private StationEntity findStationByName(String name) {
        if (stationDao.existsByName(name)) {
            return stationDao.findByName(name).get(); // TODO: 2023-05-15 if 분기 없애고 하나로 합치기
        }
        throw new StationNotFoundException();
    }

    private void validateHasStation(boolean hasStartStation, boolean hasEndStation) {
        if (hasStartStation && hasEndStation) {
            throw new IllegalSectionException("이미 노선에 추가할 역이 존재합니다.");
        }
        if (!hasStartStation && !hasEndStation) {
            throw new IllegalSectionException("노선에 기준이 되는 역을 찾을 수 없습니다.");
        }
    }

    private void validateDistance(int distance, int newDistance) {
        if (distance <= newDistance) {
            throw new IllegalDistanceException("새로운 구간의 길이는 기존 구간의 길이보다 작아야 합니다.");
        }
    }

    private void addEndStation(Line line, Section sectionToAdd) {
        Optional<Section> prevExistingSection = line.findSectionWithStartStation(sectionToAdd.getStartStation());
        prevExistingSection.ifPresent(prevSection -> {
            validateDistance(prevSection.getDistance(), sectionToAdd.getDistance());
            sectionRepository.delete(prevSection.getId());
            sectionRepository.save(line.getId(), new Section(
                    sectionToAdd.getEndStation(),
                    prevSection.getEndStation(),
                    prevSection.getDistance() - sectionToAdd.getDistance()
            ));
        });
        sectionRepository.save(line.getId(), sectionToAdd);
    }

    private void addStartStation(Line line, Section sectionToAdd) {
        Optional<Section> prevExistingSection = line.findSectionWithEndStation(sectionToAdd.getEndStation());
        prevExistingSection.ifPresent(prevSection -> {
            validateDistance(prevSection.getDistance(), sectionToAdd.getDistance());
            sectionRepository.delete(prevSection.getId());
            sectionRepository.save(line.getId(), new Section(
                            prevSection.getStartStation(),
                            sectionToAdd.getStartStation(),
                            prevSection.getDistance() - sectionToAdd.getDistance()
                    ));
                }
        );
        sectionRepository.save(line.getId(), sectionToAdd);
    }

    public void deleteSection(Long lineId, SectionDeleteRequest sectionDeleteRequest) {
        LineEntity line = findLineById(lineId);
        String stationName = sectionDeleteRequest.getStationName();
        StationEntity station = findStationByName(stationName);
        validateStationInLine(line.getId(), station);
        Optional<SectionEntity> startSection = sectionDao.findByEndStationIdAndLineId(station.getId(), line.getId());
        Optional<SectionEntity> endSection = sectionDao.findByStartStationIdAndLineId(station.getId(), line.getId());
        if (startSection.isPresent() && endSection.isPresent()) {
            mergeSection(startSection.get(), endSection.get());
            return;
        }
        endSection.ifPresent(section -> sectionDao.deleteById(section.getId()));
        startSection.ifPresent(section -> sectionDao.deleteById(section.getId()));
    }

    private void validateStationInLine(Long lineId, StationEntity station) {
        if (!sectionDao.isStationInLineById(lineId, station.getId())) {
            throw new StationNotFoundException();
        }
    }

    private void mergeSection(SectionEntity startSection, SectionEntity endSection) {
        sectionDao.deleteById(startSection.getId());
        endSection.updateDistance(startSection.getDistance() + endSection.getDistance());
        endSection.updateStartStationId(startSection.getStartStationId());
        sectionDao.update(endSection);
    }

    @Transactional(readOnly = true)
    public List<SectionResponse> findSectionsByLineId(Long lineId) {
        Line line = lineRepository.findById(lineId);
        return line.getSections().stream()
                .map(SectionResponse::from)
                .collect(toList());
    }
}
