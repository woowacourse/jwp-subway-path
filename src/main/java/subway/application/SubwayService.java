package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.SectionDao;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.StationEnrollRequest;
import subway.dto.StationResponse;
import subway.entity.SectionEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubwayService {

    private final SectionDao sectionDao;
    private final LineService lineService;
    private final StationService stationService;

    public SubwayService(SectionDao sectionDao, LineService lineService, StationService stationService) {
        this.sectionDao = sectionDao;
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public void enrollStation(Long lineId, StationEnrollRequest request) {
        final Line line = makeLineBy(lineId);

        Section section = new Section(
                stationService.findById(request.getFromStation()),
                stationService.findById(request.getToStation()),
                new Distance(request.getDistance())
        );
        line.addSection(section);
        sectionDao.save(toEntities(line.getId(), line.getSections()));
    }

    private List<SectionEntity> toEntities(final Long lineId, final List<Section> sections) {
        return sections.stream()
                .map(section -> new SectionEntity(
                        lineId,
                        section.getLeft().getName(),
                        section.getRight().getName(),
                        section.getDistance().getDistance())
                )
                .collect(Collectors.toList());
    }

    private Line makeLineBy(final Long lineId) {
        final Line line = lineService.findLineById(lineId);
        final List<Section> sections = findSections(lineId);
        return new Line(line.getId(), line.getName(), line.getColor(), sections);
    }

    private List<Section> findSections(final Long lineId) {
        final List<SectionEntity> sectionEntities = sectionDao.findById(lineId);
        final List<Station> stations = findStationsOf(sectionEntities);
        return sectionEntities.stream()
                .map(sectionEntity -> toSection(sectionEntity, stations))
                .collect(Collectors.toList());
    }

    private List<Station> findStationsOf(List<SectionEntity> sectionEntities) {
        final HashSet<String> stationNames = new HashSet<>();
        for (SectionEntity sectionEntity : sectionEntities) {
            stationNames.add(sectionEntity.getLeft());
            stationNames.add(sectionEntity.getRight());
        }
        return stationService.findStationsOf(stationNames);
    }

    private Section toSection(final SectionEntity sectionEntity, final List<Station> stations) {
        final Station leftStation = stations.stream().filter(station -> station.getName().equals(sectionEntity.getLeft())).findAny()
                .orElseThrow(() -> new IllegalArgumentException("Section 에 등록된 역이 존재하지 않습니다."));
        final Station rightStation = stations.stream().filter(station -> station.getName().equals(sectionEntity.getRight())).findAny()
                .orElseThrow(() -> new IllegalArgumentException("Section 에 등록된 역이 존재하지 않습니다."));
        return new Section(leftStation, rightStation, new Distance(sectionEntity.getDistance()));
    }

    public void deleteStation(Long lineId, Long stationId) {
        final Line line = makeLineBy(lineId);
        line.deleteStation(stationService.findById(stationId));
        sectionDao.save(toEntities(lineId, line.getSections()));
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findRouteMap(Long lineId) {
        Line line = makeLineBy(lineId);
        return line.routeMap()
                .getLineStations()
                .stream()
                .map(station -> new StationResponse(
                        station.getId(),
                        station.getName()
                )).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Map<String, List<StationResponse>> findAllRouteMap() {
        final List<Line> linesWithStations = new ArrayList<>();
        for (Line line : lineService.findLines()) {
            linesWithStations.add(makeLineBy(line.getId()));
        }
        Map<String, List<StationResponse>> allRouteMap = new HashMap<>();
        for (Line line : linesWithStations) {
            allRouteMap.put(line.getName(), findRouteMap(line.getId()));
        }
        return allRouteMap;
    }
}
