package subway.application;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Section;
import subway.domain.Station;
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
        List<Long> endStationIds = new LinkedList<>();
        for (Section section : sections) {
            findOnlyOneId(endStationIds, section.getUpStationId());
            findOnlyOneId(endStationIds, section.getDownStationId());
        }

        return sections.stream()
                .filter(section ->
                        section.getUpStationId() == endStationIds.get(0)
                                || section.getUpStationId() == endStationIds.get(1))
                .findAny().orElseThrow(() -> new IllegalStateException("호선에 역이 존재하지 않습니다."));
    }

    private void findOnlyOneId(final List<Long> result, final Long stationId) {
        if (result.contains(stationId)) {
            result.remove(stationId);
            return;
        }
        result.add(stationId);
    }

    private Section findNextStation(List<Section> sections, Long stationId) {
        return sections.stream()
                .filter(section -> section.getUpStationId() == stationId)
                .findAny()
                .orElse(null);
    }
}
