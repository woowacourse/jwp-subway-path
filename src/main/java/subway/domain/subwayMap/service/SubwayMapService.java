package subway.domain.subwayMap.service;

import org.springframework.stereotype.Service;
import subway.domain.lineDetail.domain.LineDetail;
import subway.domain.station.domain.Station;
import subway.domain.station.service.StationService;
import subway.domain.subwayMap.domain.SectionToStationConverter;
import subway.domain.subwayMap.dto.SubwayMapForLineResponse;
import subway.domain.lineDetail.dto.LineDetailResponse;
import subway.domain.lineDetail.service.LineDetailService;
import subway.domain.section.domain.Section;
import subway.domain.section.service.CreateSectionService;
import subway.domain.subwayMap.domain.SubwayMap;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubwayMapService {

    private final LineDetailService lineDetailService;
    private final StationService stationService;
    private final CreateSectionService createSectionService;
    private final SubwayMap subwayMap;

    public SubwayMapService(final LineDetailService lineDetailService, final StationService stationService, final CreateSectionService createSectionService, final SubwayMap subwayMap) {
        this.lineDetailService = lineDetailService;
        this.stationService = stationService;
        this.createSectionService = createSectionService;
        this.subwayMap = subwayMap;
    }

    public void addStation(Long lineId,Long baseStationId, Long addStationId ,boolean direction) {
        subwayMap.addStation(lineDetailService.findLineById(lineId),
                stationService.findStationById(baseStationId),
                stationService.findStationById(addStationId),
                direction);
    }

    public void deleteStation(Long lineId,Long stationId) {
        subwayMap.deleteStation(lineDetailService.findLineById(lineId),
                stationService.findStationById(stationId));
    }

    public List<SubwayMapForLineResponse> findAllSubwayMap() {
        final List<LineDetail> lineDetails = lineDetailService.findAllLine();
        return lineDetails.stream()
                .map(this::findSubwayMapByLine)
                .collect(Collectors.toList());
    }

    private SubwayMapForLineResponse findSubwayMapByLine(final LineDetail lineDetail) {
        return new SubwayMapForLineResponse(new LineDetailResponse(lineDetail.getId(), lineDetail.getName(), lineDetail.getColor()), subwayMap.getSubwayMapByLine(lineDetail));
    }

    public SubwayMapForLineResponse findSubwayMapByLineId(final Long id) {
        LineDetail lineDetail = lineDetailService.findLineById(id);
        return new SubwayMapForLineResponse(new LineDetailResponse(lineDetail.getId(), lineDetail.getName(), lineDetail.getColor()), subwayMap.getSubwayMapByLine(lineDetail));
    }

    @PostConstruct
    private void initialize() {
        final List<LineDetail> lineDetails = lineDetailService.findAllLine();
        for (LineDetail lineDetail : lineDetails) {
            final List<Section> sections = createSectionService.findSectionsByLineId(lineDetail.getId());
            final SectionToStationConverter sectionToStationConverter = SectionToStationConverter.of(sections);
            final List<Station> stations = sectionToStationConverter.getSortedStation();
            subwayMap.put(lineDetail, stations);
        }
    }
}
