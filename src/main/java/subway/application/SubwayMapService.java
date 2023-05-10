package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.LineMap;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.entity.SectionEntity;
import subway.domain.entity.StationEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubwayMapService {

    private final StationService stationService;
    private final SectionService sectionService;

    public SubwayMapService(final StationService stationService, final SectionService sectionService) {
        this.stationService = stationService;
        this.sectionService = sectionService;
    }

    @Transactional(readOnly = true)
    public List<StationEntity> showLineMap(final Long lineNumber) {
        Sections sections = getSections(lineNumber);
        LineMap lineMap = new LineMap(sections);

        List<Station> endPointStations = lineMap.getEndPointStations();

        Station upStationEndPoint = sections.getSections().stream()
                .flatMap(section -> endPointStations.stream()
                        .filter(station -> station.equals(section.getUpStation())))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("상행 종점이 존재하지 않습니다."));


        List<Station> orderedStations = lineMap.getOrderedStations(upStationEndPoint);

        return orderedStations.stream()
                .map(station -> stationService.findStationByName(station.getName()))
                .collect(Collectors.toList());

    }

    private Sections getSections(final Long lineNumber) {
        List<SectionEntity> sectionEntities = sectionService.findSectionsByLineNumber(lineNumber);

        List<Section> sections = sectionEntities.stream()
                .map(section -> {
                    StationEntity upStationEntity = stationService.findStationEntityById(section.getUpStationId());
                    StationEntity downStationEntity = stationService.findStationEntityById(section.getDownStationId());
                    Station upStation = new Station(upStationEntity.getName());
                    Station downStation = new Station(downStationEntity.getName());
                    return new Section(upStation, downStation, section.getDistance());
                })
                .collect(Collectors.toList());

        return new Sections(sections);
    }
}
