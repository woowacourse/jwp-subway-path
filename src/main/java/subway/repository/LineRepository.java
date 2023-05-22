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
        init();
    }

    public Line insert(Line line) {
        validateDuplicate(line);
        Line storedLine = lineDao.insert(line);
        store.put(storedLine.getId(), storedLine);
        return storedLine;
    }

    private void validateDuplicate(final Line line) {
        boolean isPresent = store.values().stream()
                .filter(iter -> iter.getName().equals(line.getName()))
                .findAny()
                .isPresent();
        if (isPresent) {
            throw new IllegalArgumentException("이미 존재하는 호선입니다.");
        }
    }

    public List<Line> findAll() {
        return new ArrayList<>(store.values());
    }

    private void init() {
        List<Line> lines = lineDao.findAll();
        for (Line line : lines) {
            store.put(line.getId(), line);
        }
    }

    public Line findById(Long id) {
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
