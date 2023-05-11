package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDAO;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.LineSections;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.SubwayResponse;

@Service
public class SubwayService {
    
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDAO sectionDAO;
    
    public SubwayService(final LineDao lineDao, final StationDao stationDao, final SectionDAO sectionDAO) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDAO = sectionDAO;
    }
    
    public SubwayResponse findAllStationsInLine(final long lineId) {
        final Line line = this.lineDao.findById(lineId);
        final List<Section> sections = this.sectionDAO.findSectionsBy(lineId);
        final LineSections lineSections = LineSections.from(sections);
        
        if (lineSections.isEmpty()) {
            return SubwayResponse.of(line, List.of());
        }
        
        final long upTerminalStationId = lineSections.getUpTerminalStationId();
        final long downTerminalStationId = lineSections.getDownTerminalStationId();
        
        final List<Station> orderedStations = this.generateOrderedStations(sections, upTerminalStationId,
                downTerminalStationId);
        
        return SubwayResponse.of(line, orderedStations);
    }
    
    private List<Station> generateOrderedStations(final List<Section> sections, final long upTerminalStationId,
            final long downTerminalStationId) {
        final Graph<Long, DefaultWeightedEdge> subwayMap = this.generateSubwayMap(
                sections);
        final List<Long> orderedStationIds = new DijkstraShortestPath<>(subwayMap)
                .getPath(upTerminalStationId, downTerminalStationId)
                .getVertexList();
        return orderedStationIds.stream()
                .map(this.stationDao::findById)
                .collect(Collectors.toUnmodifiableList());
    }
    
    private Graph<Long, DefaultWeightedEdge> generateSubwayMap(final List<Section> sections) {
        final Graph<Long, DefaultWeightedEdge> subwayMap = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        for (final Section section : sections) {
            subwayMap.addVertex(section.getUpStationId());
            subwayMap.addVertex(section.getDownStationId());
            final DefaultWeightedEdge edge = subwayMap.addEdge(section.getUpStationId(),
                    section.getDownStationId());
            subwayMap.setEdgeWeight(edge, section.getDistance());
        }
        return subwayMap;
    }
    
    public List<SubwayResponse> findAllStations() {
        final List<Line> lines = this.lineDao.findAll();
        return lines.stream()
                .map(line -> this.findAllStationsInLine(line.getId()))
                .collect(Collectors.toUnmodifiableList());
    }
}

