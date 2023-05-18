package subway.domain.line;

import subway.exception.DuplicatedLineNameException;
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
        validateDuplicatedName(line.getName());
        lines.put(line.getId(), line);
    }

    public void add(Collection<Line> lines) {
        lines.forEach(existLine -> {
            validateDuplicatedName(existLine.getName());
            this.lines.put(existLine.getId(), existLine);
        });
    }

    private void validateDuplicatedName(final String name) {
        if (lines.values().stream().anyMatch(line -> line.getName().equals(name))) {
            throw new DuplicatedLineNameException(name);
        }
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
