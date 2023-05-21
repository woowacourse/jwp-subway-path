package subway.application;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.LineSections;
import subway.dto.SubwayResponse;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubwayService {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDAO;

    public SubwayService(final LineDao lineDao, final StationDao stationDao, final SectionDao sectionDAO) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDAO = sectionDAO;
    }

    public SubwayResponse findAllStationsInLine(final long lineId) {
        final LineEntity lineEntity = this.lineDao.findById(lineId);
        final List<SectionEntity> sectionEntities = this.sectionDAO.findSectionsBy(lineId);
        final LineSections lineSections = LineSections.from(sectionEntities);

        if (lineSections.isEmpty()) {
            return SubwayResponse.of(lineEntity, List.of());
        }

        final long upTerminalStationId = lineSections.getUpTerminalStationId();
        final long downTerminalStationId = lineSections.getDownTerminalStationId();

        final List<StationEntity> orderedStationEntities = this.generateOrderedStations(sectionEntities, upTerminalStationId,
                downTerminalStationId);

        return SubwayResponse.of(lineEntity, orderedStationEntities);
    }

    private List<StationEntity> generateOrderedStations(final List<SectionEntity> sectionEntities, final long upTerminalStationId,
                                                        final long downTerminalStationId) {
        final Graph<Long, DefaultWeightedEdge> subwayMap = this.generateSubwayMap(
                sectionEntities);
        final List<Long> orderedStationIds = new DijkstraShortestPath<>(subwayMap)
                .getPath(upTerminalStationId, downTerminalStationId)
                .getVertexList();
        return orderedStationIds.stream()
                .map(this.stationDao::findById)
                .collect(Collectors.toUnmodifiableList());
    }

    private Graph<Long, DefaultWeightedEdge> generateSubwayMap(final List<SectionEntity> sectionEntities) {
        final Graph<Long, DefaultWeightedEdge> subwayMap = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        for (final SectionEntity sectionEntity : sectionEntities) {
            subwayMap.addVertex(sectionEntity.getUpStationId());
            subwayMap.addVertex(sectionEntity.getDownStationId());
            final DefaultWeightedEdge edge = subwayMap.addEdge(sectionEntity.getUpStationId(),
                    sectionEntity.getDownStationId());
            subwayMap.setEdgeWeight(edge, sectionEntity.getDistance());
        }
        return subwayMap;
    }

    public List<SubwayResponse> findAllStations() {
        final List<LineEntity> lineEntities = this.lineDao.findAll();
        return lineEntities.stream()
                .map(line -> this.findAllStationsInLine(line.getId()))
                .collect(Collectors.toUnmodifiableList());
    }
}

