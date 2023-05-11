package subway.business.domain;

import java.util.LinkedList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryLineRepository implements LineRepository {
    private final List<Line> lines;

    public InMemoryLineRepository(List<Line> lines) {
        this.lines = new LinkedList<>();
    }

    @Override
    public long save(Line line) {
        lines.add(line);
        return lines.indexOf(line);
    }

    @Override
    public Line findById(Long id) {
        return lines.get(id.intValue());
    }

    @Override
    public List<Line> findAll() {
        return this.lines;
    }
}
