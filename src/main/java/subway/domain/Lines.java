package subway.domain;

import subway.exception.StationNotFoundException;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Lines {

    private final Map<Long, Line> lines;

    public Lines() {
        this.lines = new HashMap<>();
    }

    public void add(Line line) {
        lines.put(line.getId(), line);
    }

    public void add(Collection<Line> lines) {
        lines.forEach(this::add);
    }

    public Line get(Long id) {
        validateIsExist(id);
        return lines.get(id);
    }

    private void validateIsExist(final Long id) {
        if (!lines.containsKey(id)) {
            throw new StationNotFoundException();
        }
    }

    public Set<Line> toSet() {
        return new HashSet<>(lines.values());
    }
}
