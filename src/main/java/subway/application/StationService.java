package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.dto.StationDeleteRequest;
import subway.dto.StationResponse;
import subway.dto.StationSaveRequest;
import subway.domain.FinalStation;
import subway.domain.Section;
import subway.domain.Station;

@Transactional(readOnly = true)
@Service
public class StationService {

    private final SectionService sectionService;
    private final StationDao stationDao;

    public StationService(final SectionService sectionService, final StationDao stationDao) {
        this.sectionService = sectionService;
        this.stationDao = stationDao;
    }

    @Transactional
    public void saveStation(StationSaveRequest request) {
        validateAlreadyExistRequestStations(List.of(request.getUpStation(), request.getDownStation()));
        if (checkEmptyStations()) {
            saveAllRequestStations(request);
            return;
        }

        if (isAlreadyExistStation(request.getUpStation())) {
            nonCalculateDistanceAndCreateStation(request);
            return;
        }

        if (isAlreadyExistStation(request.getDownStation())) {
            createSection(request);
        }
    }

    private void nonCalculateDistanceAndCreateStation(final StationSaveRequest request) {
        final Station alreadyExistStation = stationDao.findByName(request.getUpStation()).get();
        FinalStation finalStation = getFinalStationInfo(request);

        if (finalStation.isNotFinalStation(alreadyExistStation.getName()) || finalStation.isFinalUpStation(alreadyExistStation.getName())) {
            calculateDistanceAndCreateStation(request, alreadyExistStation);
            return;
        }
        nonCalculateDistanceAndCreateStation(request, alreadyExistStation);
    }

    private void nonCalculateDistanceAndCreateStation(final StationSaveRequest request, final Station alreadyExistStation) {
        final Long newUpStationId = alreadyExistStation.getId();
        final Long newDownStationId = stationDao.insert(request.getDownStationEntity());
        sectionService.saveSection(
                Section.of(request.getLineId(), newUpStationId, newDownStationId, request.getDistance()));
    }

    private void calculateDistanceAndCreateStation(final StationSaveRequest request, final Station alreadyExistStation) {
        final Section originSection = sectionService.findSectionByUpStationId(alreadyExistStation.getId());
        validateDistance(originSection.getDistance(), request.getDistance());
        sectionService.deleteById(originSection.getId());
        createNewSections(request, originSection, alreadyExistStation);
    }

    private FinalStation getFinalStationInfo(StationSaveRequest request) {
        final String finalUpStationName = stationDao.getUpTerminalStation(request.getLineId()).getName();
        final String finalDownStationName = stationDao.getDownTerminalStation(request.getLineId()).getName();
        return FinalStation.of(finalUpStationName, finalDownStationName);
    }

    private void createSection(final StationSaveRequest request) {
        final Long upStationId = stationDao.insert(request.getUpStationEntity());
        final Long downStationId = stationDao.findByName(request.getDownStation()).get().getId();

        sectionService.saveSection(
                Section.of(request.getLineId(), upStationId, downStationId, request.getDistance())
        );
    }

    private void createNewSections(StationSaveRequest request, Section section, Station alreadyExistStation) {
        final Long downStationId = stationDao.insert(request.getDownStationEntity());
        final int beforeSectionDistance = section.getDistance() - request.getDistance();

        sectionService.saveSection(Section.of(
                request.getLineId(),
                alreadyExistStation.getId(),
                downStationId,
                beforeSectionDistance
        ));
        sectionService.saveSection(Section.of(
                request.getLineId(),
                downStationId,
                section.getDownStationId(),
                section.getDistance() - beforeSectionDistance
        ));
    }

    private void saveAllRequestStations(StationSaveRequest request) {
        final Long upStationId = stationDao.insert(request.getUpStationEntity());
        final Long downStationId = stationDao.insert(request.getDownStationEntity());

        sectionService.saveSection(Section.of(
                request.getLineId(),
                upStationId,
                downStationId,
                request.getDistance())
        );
    }

    private static void validateDistance(final int originDistance, final int newDistance) {
        if (originDistance - newDistance < 0) {
            throw new IllegalArgumentException("거리는 기존 구간의 길이보다 작아야합니다.");
        }
    }

    private boolean isAlreadyExistStation(final String stationName) {
        return stationDao.findByName(stationName).isPresent();
    }

    private void validateAlreadyExistRequestStations(final List<String> stationNames) {
        for (String name : stationNames) {
            if (stationDao.findByName(name).isEmpty()) {
                return;
            }
        }
        throw new IllegalArgumentException("입력한 역들이 모두 존재합니다.");
    }

    private boolean checkEmptyStations() {
        final List<Station> allStations = stationDao.findAll();
        System.out.println(allStations.size());
        return allStations.size() == 0;
    }

    public StationResponse findStationResponseById(Long id) {
        return StationResponse.of(stationDao.findById(id));
    }

    public List<StationResponse> findAllStationResponses() {
        List<Station> stations = stationDao.findAll();

        return stations.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    public List<StationResponse> findAllStationOrderBySection() {
        return stationDao.findAll().stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(final StationDeleteRequest request) {
        final List<Section> allSections = sectionService.findAll();

        final Section connectedOnLeft = getConnectedOnLeft(allSections, request.getStationId());

        final Section connectedOnRight = getConnectedOnRight(allSections, request.getStationId());

        if (connectedOnLeft == null || connectedOnRight == null) {
            stationDao.deleteById(request.getStationId());
            return;
        }

        int newDistance = connectedOnLeft.getDistance() + connectedOnRight.getDistance();

        sectionService.deleteById(connectedOnLeft.getId());
        sectionService.deleteById(connectedOnRight.getId());
        stationDao.deleteById(request.getStationId());
        sectionService.saveSection(
                Section.of(request.getLineId(),
                        connectedOnLeft.getUpStationId(),
                        connectedOnRight.getDownStationId(),
                        newDistance));
    }

    private Section getConnectedOnLeft(final List<Section> allSections, final Long id) {
        final List<Section> sections = allSections.stream()
                .filter(it -> it.getDownStationId() == id)
                .collect(Collectors.toList());
        if (sections.size() == 0) {
            return null;
        }

        return sections.get(0);
    }

    private Section getConnectedOnRight(final List<Section> allSections, final Long id) {
        final List<Section> sections = allSections.stream()
                .filter(it -> it.getUpStationId() == id)
                .collect(Collectors.toList());
        if (sections.size() == 0) {
            return null;
        }

        return sections.get(0);
    }

    public void updateStation(final Long id, final StationSaveRequest request) {
    }
}
