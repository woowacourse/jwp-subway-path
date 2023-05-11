package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.dto.StationDeleteRequest;
import subway.dto.StationResponse;
import subway.dto.StationSaveRequest;
import subway.entity.Section;
import subway.entity.Station;

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
        final List<String> stationNames = List.of(
                request.getUpStation(),
                request.getDownStation());

        validateAlreadyExistRequestStations(stationNames);
        if (checkEmptyStations()) {
            final Long upStationId = stationDao.insert(request.getUpStationEntity());
            final Long downStationId = stationDao.insert(request.getDownStationEntity());

            sectionService.saveSection(Section.of(
                    request.getLineId(),
                    upStationId,
                    downStationId,
                    request.getDistance())
            );
            return;
        }

        if (isAlreadyExistStation(request.getUpStation())) {
            final Station baseStation = stationDao.findByName(request.getUpStation()).get();
            final Station upTerminalStation = stationDao.getUpTerminalStation(request.getLineId());
            final Station downTerminalStation = stationDao.getDownTerminalStation(request.getLineId());
            final List<Station> terminalStations = List.of(upTerminalStation, downTerminalStation);

            if (!baseStation.isContainStations(terminalStations) ||
                    baseStation.isContainStations(List.of(upTerminalStation))
            ) {

                final Section section = sectionService.findByUpStationId(baseStation.getId());
                validateDistance(request, section);

                final Long downStationId = stationDao.insert(request.getDownStationEntity());
                final int beforeSectionDistance = section.getDistance() - request.getDistance();

                final Section beforeSection = Section.of(
                        request.getLineId(),
                        baseStation.getId(),
                        downStationId,
                        beforeSectionDistance
                );

                final Section afterSection = Section.of(
                        request.getLineId(),
                        downStationId,
                        section.getDownStationId(),
                        section.getDistance() - beforeSectionDistance);

                sectionService.deleteById(section.getId());
                sectionService.saveSection(beforeSection);
                sectionService.saveSection(afterSection);
                return;
            }

            final Long newUpStationId = baseStation.getId();
            final Long newDownStationId = stationDao.insert(request.getDownStationEntity());
            sectionService.saveSection(
                    Section.of(request.getLineId(), newUpStationId, newDownStationId, request.getDistance()));
            return;
        }

        if (isAlreadyExistStation(request.getDownStation())) {
            final Long upStationId = stationDao.insert(request.getUpStationEntity());
            final Long downStationId = stationDao.findByName(request.getDownStation()).get().getId();

            sectionService.saveSection(
                    Section.of(request.getLineId(), upStationId, downStationId, request.getDistance()));
        }
    }

    private static void validateDistance(final StationSaveRequest request, final Section section) {
        if (section.getDistance() - request.getDistance() < 0) {
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
