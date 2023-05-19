package subway.application;

import org.springframework.stereotype.Service;
import subway.common.Cost;
import subway.dao.*;
import subway.domain.*;
import subway.dto.PathResponse;
import subway.dto.StationResponse;
import subway.exception.InvalidStationException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;
    private final Cost cost;

    public PathService(SectionDao sectionDao, StationDao stationDao, LineDao lineDao, Cost cost) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.cost = cost;
    }

    public PathResponse findPath(Long startStationId, Long endStationId) {
        Lines lines = new Lines();
        lineDao.findAll()
                .forEach(it -> lines.addNewLine(it.getName(), new Sections(toSections(sectionDao.findByLineId(it.getId())))));

        StationEntity startStation = stationDao.findById(startStationId)
                .orElseThrow(InvalidStationException::new);
        StationEntity endStation = stationDao.findById(endStationId)
                .orElseThrow(InvalidStationException::new);

        List<Station> stations = toStations(stationDao.findAll());
        List<Section> sections = lines.getLines().stream()
                .flatMap(it -> it.getSections().getSections().stream())
                .collect(Collectors.toList());

        Graph graph = new Graph(stations, sections);
        List<String> pathStations = graph.findPath(startStation.getName(), endStation.getName());
        double pathDistance = graph.findPathDistance(startStation.getName(), endStation.getName());

        return new PathResponse(makeStationResponses(pathStations), (int) pathDistance, cost.calculate((int)pathDistance));
    }

    private List<Station> toStations(List<StationEntity> findStations) {
        return findStations.stream()
                .map(it -> new Station(it.getName()))
                .collect(Collectors.toList());
    }

    private List<Section> toSections(List<SectionEntity> findSections) {
        Map<Long, String> stations = stationDao.findAll()
                .stream()
                .collect(Collectors.toMap(StationEntity::getId, StationEntity::getName));

        return findSections.stream()
                .map(it -> new Section(
                        new Station(stations.get(it.getStartStationId())),
                        new Station(stations.get(it.getEndStationId())),
                        new Distance(it.getDistance()))
                )
                .collect(Collectors.toList());
    }

    private List<StationResponse> makeStationResponses(List<String> stationNames) {

        Map<String, Long> stations = stationDao.findAll().stream()
                .collect(Collectors.toMap(StationEntity::getName, StationEntity::getId));

        return stationNames.stream()
                .map(it -> new StationResponse(stations.get(it), it))
                .collect(Collectors.toList());
    }
}
