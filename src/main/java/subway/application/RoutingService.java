package subway.application;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.FareCalculator;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.SubwayMap;
import subway.dto.RoutesResponse;
import subway.dto.StationResponse;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

@Transactional(readOnly = true)
@Service
public class RoutingService {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public RoutingService(final SectionRepository sectionRepository, final StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    public List<StationResponse> findStationResponses(Long lineId) {
        List<Station> result = new ArrayList<>();
        List<Section> sections = sectionRepository.findAllByLineId(lineId);

        Section nextSection = findFirstSection(sections);
        Long nextUpStationId = nextSection.getDownStationId();
        while (nextSection != null) {
            result.add(stationRepository.findById(nextSection.getUpStationId()));
            nextUpStationId = nextSection.getDownStationId();
            nextSection = findNextStation(sections, nextUpStationId);
        }
        result.add(stationRepository.findById(nextUpStationId));

        return result.stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    private Section findFirstSection(List<Section> sections) {
        List<Long> endStationIds = findEndStationIds(sections);

        return sections.stream()
                .filter(section ->
                        section.getUpStationId().equals(endStationIds.get(0)) ||
                                section.getUpStationId().equals(endStationIds.get(1)))
                .findAny().orElseThrow(() -> new IllegalStateException("호선에 역이 존재하지 않습니다."));
    }

    private List<Long> findEndStationIds(final List<Section> sections) {
        List<Long> endStationIds = new LinkedList<>();
        for (Section section : sections) {
            toggleAddRemove(endStationIds, section.getUpStationId());
            toggleAddRemove(endStationIds, section.getDownStationId());
        }
        return endStationIds;
    }

    private void toggleAddRemove(final List<Long> result, final Long stationId) {
        if (result.contains(stationId)) {
            result.remove(stationId);
            return;
        }
        result.add(stationId);
    }

    private Section findNextStation(List<Section> sections, Long stationId) {
        return sections.stream()
                .filter(section -> section.getUpStationId().equals(stationId))
                .findAny()
                .orElse(null);
    }

    public RoutesResponse findRoutes(final Long startStationId, final Long endStationId) {
        List<Section> sections = sectionRepository.findAll();
        SubwayMap subwayMap = new SubwayMap(sections);
        List<StationResponse> stationResponses = findShortestPath(subwayMap, startStationId, endStationId)
                .stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
        Double totalDistance = subwayMap.getTotalDistance(startStationId, endStationId);
        int fare = FareCalculator.calculate(totalDistance);
        return new RoutesResponse(stationResponses, totalDistance, fare);
    }

    private List<Station> findShortestPath(final SubwayMap subwayMap, final Long startStationId,
                                           final Long endStationId) {
        List<Long> stationIds = subwayMap.findShortestPathIds(startStationId, endStationId);
        List<Station> stations = new ArrayList<>(stationIds.size());
        for (Long stationId : stationIds) {
            Station station = stationRepository.findById(stationId);
            stations.add(station);
        }
        return stations;
    }
}
