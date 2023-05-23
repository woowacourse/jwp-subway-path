package subway.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import subway.domain.line.Line;

public class StubLineDao implements LineDao {

    private final Map<Long, Line> lineMap = new HashMap<>();
    private final AtomicLong maxId = new AtomicLong();

    @Override
    public Line insert(final Line line) {
        final long currentId = maxId.incrementAndGet();
        final Line saved = new Line(currentId, line);
        lineMap.put(currentId, saved);
        return saved;
    }

    @Override
    public List<Line> findAll() {
        return new ArrayList<>(lineMap.values());
    }

    @Override
    public Line findById(final Long id) {
        return lineMap.get(id);
    }

    @Override
    public void update(final Line line) {
        lineMap.put(line.getId(), line);
    }

    @Override
    public void deleteById(final Long id) {
        lineMap.remove(id);
    }
}
