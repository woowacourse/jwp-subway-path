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
    private final Graph graph;

    public PathService(SectionDao sectionDao, StationDao stationDao, LineDao lineDao, Cost cost, Graph graph) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.cost = cost;
        this.graph = graph;
    }

    public PathResponse findPath(Long startStationId, Long endStationId) {
        Lines lines = new Lines();
        lineDao.findAll()
                .forEach(it -> lines.addNewLine(new Line(it.getId(), it.getName(), new Sections(sectionDao.findByLineId(it.getId())))));

        Station startStation = stationDao.findById(startStationId)
                .orElseThrow(InvalidStationException::new);
        Station endStation = stationDao.findById(endStationId)
                .orElseThrow(InvalidStationException::new);

        List<Station> stations = stationDao.findAll();
        List<Section> sections = lines.getLines().stream()
                .flatMap(it -> it.getSections().getSections().stream())
                .collect(Collectors.toList());

        graph.set(stations,sections);
        List<String> pathStations = graph.findPath(startStation.getName(), endStation.getName());
        double pathDistance = graph.findPathDistance(startStation.getName(), endStation.getName());

        return new PathResponse(makeStationResponses(pathStations), (int) pathDistance, cost.calculate((int)pathDistance));
    }

    private List<StationResponse> makeStationResponses(List<String> stationNames) {
        Map<String, Long> stations = stationDao.findAll().stream()
                .collect(Collectors.toMap(Station::getName, Station::getId));

        return stationNames.stream()
                .map(it -> new StationResponse(stations.get(it), it))
                .collect(Collectors.toList());
    }
}
