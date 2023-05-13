package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.StationResponse;
import subway.util.FinalStationFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
public class StationService {

    private final StationDao stationDao;
    private final SectionService sectionService;
    private final FinalStationFactory finalStationFactory;

    public StationService(final StationDao stationDao, final SectionService sectionService, final FinalStationFactory finalStationFactory) {
        this.stationDao = stationDao;
        this.sectionService = sectionService;
        this.finalStationFactory = finalStationFactory;
    }

    public List<StationResponse> getAllStationResponses(final Long lineId) {
        List<Section> sections = sectionService.findAll();
        Station finalUpStation = stationDao.findFinalUpStation(lineId);
        List<Station> results = new ArrayList<>();
        results.add(finalUpStation);

        Long beforeStationId = finalUpStation.getId();

        int totalSections = sections.size();
        while (results.size() < totalSections + 1) {
            for (Section section : sections) {
                beforeStationId = updateStationIdIfSameStation(results, beforeStationId, section);
            }
        }

        return results.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    private Long updateStationIdIfSameStation(final List<Station> results, Long beforeStationId, final Section section) {
        if (section.getUpStationId() == beforeStationId) {
            Station station = stationDao.findById(section.getDownStationId());
            results.add(station);
            beforeStationId = station.getId();
        }
        return beforeStationId;
    }


    @Transactional
    public void deleteStationById(final Long lineId, final Long stationId) {
        Station station = stationDao.findById(stationId);
        if (finalStationFactory.getFinalStation(lineId).isFinalStation(station.getName())) {
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
