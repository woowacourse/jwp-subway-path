package subway.domain.line;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Lines {

    private final Map<Long, Line> lineById;

    private Lines(final Map<Long, Line> lineById) {
        this.lineById = lineById;
    }

    public static Lines of(final List<Line> lines) {
        final Lines instance = new Lines(new HashMap<>());
        lines.forEach(instance::put);
        return instance;
    }

    private void put(final Line line) {
        lineById.put(line.getId(), line);
    }

    public Line getLine(final Long id) {
        if (lineById.containsKey(id)) {
            return lineById.get(id);
        }
        throw new IllegalArgumentException("존재하지 않는 노선입니다.");
    }

    public Set<Long> getAllIds() {
        if (lineById.isEmpty()) {
            throw new IllegalStateException("노선이 하나도 존재하지 않습니다.");
        }
        return lineById.keySet();
    }
}
