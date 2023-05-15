package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.EdgeDao;
import subway.domain.edge.Edge;
import subway.domain.edge.Edges;

import java.util.List;

@Repository
public class EdgeRepository {

    private final EdgeDao edgeDao;

    public EdgeRepository(final EdgeDao edgeDao) {
        this.edgeDao = edgeDao;
    }

    public List<Edge> findAllByLineId(final Long lineId) {

        return edgeDao.findAllByLineId(lineId);
    }

    public void deleteAllByLineId(final Long lineId) {

        edgeDao.deleteAllByLineId(lineId);
    }

    public void insertAllByLineId(final Long lineId, final Edges edges) {

        edgeDao.insertAllByLineId(lineId, edges.getEdges());
    }
}
