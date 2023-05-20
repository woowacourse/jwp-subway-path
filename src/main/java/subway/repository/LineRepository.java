package subway.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;
import subway.dao.LineDao;
import subway.domain.Line;

@Repository
public class LineRepository {

    private static final HashMap<Long, Line> store = new HashMap<>();

    private final LineDao lineDao;

    public LineRepository(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public Line insert(Line line) {
        Line storedLine = lineDao.insert(line);
        store.put(storedLine.getId(), storedLine);
        return storedLine;
    }

    public List<Line> findAll() {
        init();
        return new ArrayList<>(store.values());
    }

    private void init() {
        if (store.isEmpty()) {
            List<Line> lines = lineDao.findAll();
            for (Line line : lines) {
                store.put(line.getId(), line);
            }
        }
    }

    public Line findById(Long id) {
        init();
        return store.get(id);
    }

    public void update(Line newLine) {
        lineDao.update(newLine);
        store.put(newLine.getId(), newLine);
    }

    public void deleteById(Long id) {
        lineDao.deleteById(id);
        store.remove(id);
    }
}
