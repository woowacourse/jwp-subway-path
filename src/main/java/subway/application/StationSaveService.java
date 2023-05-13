package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.domain.FinalStation;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.StationSaveRequest;
import subway.exception.DistanceCantNegative;
import subway.exception.StationsAlreadyExistException;
import subway.util.FinalStationFactory;

import java.util.List;

@Service
public class StationSaveService {

    private final StationDao stationDao;
    private final SectionService sectionService;
    private final FinalStationFactory finalStationFactory;

    public StationSaveService(final StationDao stationDao, final SectionService sectionService, final FinalStationFactory finalStationFactory) {
        this.stationDao = stationDao;
        this.sectionService = sectionService;
        this.finalStationFactory = finalStationFactory;
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
        FinalStation finalStation = finalStationFactory.getFinalStation(lineId);

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
        if (originDistance - newDistance < 1) {
            throw DistanceCantNegative.THROW;
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
        throw StationsAlreadyExistException.THROW;
    }

    private boolean checkEmptyStations() {
        final List<Station> allStations = stationDao.findAll();
        System.out.println(allStations.size());
        return allStations.size() == 0;
    }

}
