package subway.business.domain;

import java.util.LinkedList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryLineRepository implements LineRepository {
    private final List<Line> lines;

    public InMemoryLineRepository() {
        this.lines = new LinkedList<>();
    }

    @Override
    public long create(Line line) {
        lines.add(new Line((long) lines.size(), line.getName(), line.getSections()));
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

    @Override
    public void save(Line line) {
        lines.set(line.getId().intValue(), line);
    }
}
