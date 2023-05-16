package subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.EdgeDao;
import subway.dao.LineDao;
import subway.domain.Edge;
import subway.domain.Line;

@Repository
public class LineRepository {

    public static final int INITIAL_EDGE = 0;

    private final LineDao lineDao;
    private final EdgeDao edgeDao;

    public LineRepository(LineDao lineDao, EdgeDao edgeDao) {
        this.lineDao = lineDao;
        this.edgeDao = edgeDao;
    }

    public Line getLine(Long lineId) {
        return assembleLine(lineId);
    }

    public List<Line> getAllLines() {
        return lineDao.findAll()
                .stream()
                .map(line -> assembleLine(line.getId()))
                .collect(Collectors.toList());
    }

    public Line insertNewLine(Line createdLine) {
        Long lineId = lineDao.insert(createdLine);
        edgeDao.insert(lineId, createdLine.getEdges().get(INITIAL_EDGE));
        return assembleLine(lineId);
    }

    public Line updateLine(Line updatedLine) {
        edgeDao.deleteAllByLineId(updatedLine.getId());
        edgeDao.insertAllByLineId(updatedLine.getId(), updatedLine.getEdges());
        return assembleLine(updatedLine.getId());
    }

    public void checkLineIsExist(String lineName) {
        lineDao.findByName(lineName)
                .ifPresent(line -> {
                    throw new IllegalArgumentException("이미 존재하는 노선입니다.");
                });
    }

    private Line assembleLine(Long lineId) {
        Line line = lineDao.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));

        List<Edge> edges = edgeDao.findEdgesByLineId(line.getId());
        return new Line(line.getId(), line.getName(), edges);
    }
}
