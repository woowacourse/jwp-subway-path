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
        
        final List<Long> upStationIds = sections.stream()
                .map(Section::getUpStationId)
                .collect(Collectors.toUnmodifiableList());
        final List<Long> downStationIds = sections.stream()
                .map(Section::getDownStationId)
                .collect(Collectors.toUnmodifiableList());
        
        final Long firstStationId = upStationIds.stream()
                .filter(i -> !downStationIds.contains(i))
                .findFirst()
                .orElseThrow();
        final Long lastStationId = downStationIds.stream()
                .filter(i -> !upStationIds.contains(i))
                .findFirst()
                .orElseThrow();
        
        final Graph<Long, DefaultWeightedEdge> subwayMap = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        for (final Section section : sections) {
            subwayMap.addVertex(section.getUpStationId());
            subwayMap.addVertex(section.getDownStationId());
            final DefaultWeightedEdge edge = subwayMap.addEdge(section.getUpStationId(),
                    section.getDownStationId());
            subwayMap.setEdgeWeight(edge, section.getDistance());
        }
        
        final List<Long> orderedStationIds = new DijkstraShortestPath<>(subwayMap).getPath(firstStationId,
                        lastStationId)
                .getVertexList();
        final List<Station> orderedStations = orderedStationIds.stream()
                .map(this.stationDao::findById)
                .collect(Collectors.toUnmodifiableList());
        
        return SubwayResponse.of(line, orderedStations);
    }
    
    public List<SubwayResponse> findAllStations() {
        final List<Line> lines = this.lineDao.findAll();
        return lines.stream()
                .map(line -> this.findAllStationsInLine(line.getId()))
                .collect(Collectors.toUnmodifiableList());
    }
}

