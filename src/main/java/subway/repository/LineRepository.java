package subway.repository;

import org.springframework.stereotype.Repository;
import subway.dao.EdgeDao;
import subway.dao.LineDao;
import subway.domain.edge.Edge;
import subway.domain.edge.Edges;
import subway.domain.line.Line;
import subway.exception.LineNotFoundException;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class LineRepository {

    private final LineDao lineDao;
    private final EdgeDao edgeDao;

    public LineRepository(final LineDao lineDao, final EdgeDao edgeDao) {
        this.lineDao = lineDao;
        this.edgeDao = edgeDao;
    }


    public Optional<Line> findLineByName(final Line line) {

        return lineDao.findByName(line.getName());
    }

    public Line save(final Line line) {
        final Line insertedLine = lineDao.insert(line.getName());
        edgeDao.insertAllByLineId(insertedLine.getId(), line.getEdges().getEdges());

        return insertedLine;
    }

    public Optional<Line> findLineWithEdgesByLineId(final Long lineId) {
        final Line findLine = lineDao.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException(lineId));
        final List<Edge> findEdges = edgeDao.findAllByLineId(findLine.getId());
        final Edges edges = new Edges(findEdges);

        return Optional.of(new Line(findLine.getId(), findLine.getName(), edges));
    }

    public Optional<Line> findById(final Long lineId) {
        return lineDao.findById(lineId);
    }

    public List<Line> findAllWithEdges() {
        final List<Line> allLine = lineDao.findAll();

        return allLine.stream()
                .map(line -> new Line(
                        line.getId(), line.getName(),
                        new Edges(new LinkedList<>(edgeDao.findAllByLineId(line.getId())))))
                .collect(Collectors.toList());
    }
}
