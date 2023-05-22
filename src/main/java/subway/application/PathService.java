package subway.application;

import org.springframework.stereotype.Service;
import subway.common.Cost;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.path.Graph;
import subway.domain.path.GraphGenerator;
import subway.domain.path.Path;
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
    private final GraphGenerator generatedGraph;

    public PathService(SectionDao sectionDao, StationDao stationDao, LineDao lineDao, Cost cost, GraphGenerator generatedGraph) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
        this.cost = cost;
        this.generatedGraph = generatedGraph;
    }

    public PathResponse findPath(Long startStationId, Long endStationId) {
        Station startStation = stationDao.findById(startStationId)
                .orElseThrow(InvalidStationException::new);
        Station endStation = stationDao.findById(endStationId)
                .orElseThrow(InvalidStationException::new);

        List<Station> stations = stationDao.findAll();

        List<Section> sections = lineDao.findAll().stream()
                .map(it -> new Line(it.getId(), it.getName(), new Sections(sectionDao.findByLineId(it.getId()))))
                .flatMap(it -> it.getSections().getSections().stream())
                .collect(Collectors.toList());

        Graph graph = generatedGraph.generate();
        Path path = graph.findPath(stations, sections, startStation.getName(), endStation.getName());

        return new PathResponse(makeStationResponses(path.getStations()), (int) path.getTotalDistance(), cost.calculate(path.getTotalDistance()));
    }

    private List<StationResponse> makeStationResponses(List<String> stationNames) {
        Map<String, Long> stations = stationDao.findAll().stream()
                .collect(Collectors.toMap(Station::getName, Station::getId));

        return stationNames.stream()
                .map(it -> new StationResponse(stations.get(it), it))
                .collect(Collectors.toList());
    }
}
