package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.domain.FinalStation;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.StationResponse;
import subway.dto.StationSaveRequest;

import java.util.List;
import java.util.stream.Collectors;

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
    public void saveStation(final Long lineId, final StationSaveRequest request) {
        validateAlreadyExistRequestStations(List.of(request.getUpStation(), request.getDownStation()));
        if (checkEmptyStations()) {
            saveAllRequestStations(lineId, request);
            return;
        }

        if (isAlreadyExistStation(request.getUpStation())) {
            createAnotherStation(lineId, request);
            return;
        }

        if (isAlreadyExistStation(request.getDownStation())) {
            createSection(lineId, request);
        }
    }

    private void createAnotherStation(final Long lineId, final StationSaveRequest request) {
        final Station alreadyExistStation = stationDao.findByName(request.getUpStation()).get();
        FinalStation finalStation = getFinalStation(lineId);

        if (finalStation.isNotFinalStation(alreadyExistStation.getName()) || finalStation.isFinalUpStation(alreadyExistStation.getName())) {
            calculateDistanceAndCreateStation(lineId, request, alreadyExistStation);
            return;
        }
        nonCalculateDistanceAndCreateStation(lineId, request, alreadyExistStation);
    }

    private void nonCalculateDistanceAndCreateStation(final Long lineId, final StationSaveRequest request, final Station alreadyExistStation) {
        final Long newUpStationId = alreadyExistStation.getId();
        final Long newDownStationId = stationDao.insert(request.getDownStationEntity());
        sectionService.saveSection(
                Section.of(lineId, newUpStationId, newDownStationId, request.getDistance()));
    }

    private void calculateDistanceAndCreateStation(final Long lineId, final StationSaveRequest request, final Station alreadyExistStation) {
        final Section originSection = sectionService.findSectionByUpStationId(alreadyExistStation.getId());
        validateDistance(originSection.getDistance(), request.getDistance());
        sectionService.deleteById(originSection.getId());
        createNewSections(lineId, request, originSection, alreadyExistStation);
    }

    private FinalStation getFinalStation(final Long lineId) {
        final String finalUpStationName = stationDao.findUpTerminalStation(lineId).getName();
        final String finalDownStationName = stationDao.findDownTerminalStation(lineId).getName();
        return FinalStation.of(finalUpStationName, finalDownStationName);
    }

    private void createSection(final Long lineId, final StationSaveRequest request) {
        final Long upStationId = stationDao.insert(request.getUpStationEntity());
        final Long downStationId = stationDao.findByName(request.getDownStation()).get().getId();

        sectionService.saveSection(
                Section.of(lineId, upStationId, downStationId, request.getDistance())
        );
    }

    private void createNewSections(final Long lineId, final StationSaveRequest request, final Section section, final Station alreadyExistStation) {
        final Long downStationId = stationDao.insert(request.getDownStationEntity());
        final int beforeSectionDistance = section.getDistance() - request.getDistance();

        sectionService.saveSection(Section.of(
                lineId,
                alreadyExistStation.getId(),
                downStationId,
                beforeSectionDistance
        ));
        sectionService.saveSection(Section.of(
                lineId,
                downStationId,
                section.getDownStationId(),
                section.getDistance() - beforeSectionDistance
        ));
    }

    private void saveAllRequestStations(final Long lineId, final StationSaveRequest request) {
        final Long upStationId = stationDao.insert(request.getUpStationEntity());
        final Long downStationId = stationDao.insert(request.getDownStationEntity());

        sectionService.saveSection(Section.of(
                lineId,
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

    public StationResponse findStationResponseById(final Long id) {
        return StationResponse.of(stationDao.findById(id));
    }

    public List<StationResponse> getAllStationResponses() {
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
    public void deleteStationById(final Long lineId, final Long stationId) {
        Station station = stationDao.findById(stationId);
        if (getFinalStation(lineId).isFinalStation(station.getName())) {
            stationDao.deleteById(stationId);
            return;
        }

        final Section leftSection = sectionService.getLeftSectionByStationId(stationId);
        final Section rightSection = sectionService.getRightSectionByStationId(stationId);
        int newDistance = leftSection.getDistance() + rightSection.getDistance();

        stationDao.deleteById(stationId);
        sectionService.saveSection(
                Section.of(
                        lineId,
                        leftSection.getUpStationId(),
                        rightSection.getDownStationId(),
                        newDistance
                )
        );
    }

}
