package subway.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;
import subway.domain.Line;

@Repository
public class SimpleLineRepository implements LineRepository {

    private Long idIndex;

    private final List<Line> lines;

    public SimpleLineRepository() {
        this.idIndex = 1L;
        this.lines = new ArrayList<>();
    }

    @Override
    public Long create(Line line) {
        lines.add(Line.of(idIndex, line.getName(), line.getColor(), line.getStationEdges()));
        return idIndex++;
    }
}
