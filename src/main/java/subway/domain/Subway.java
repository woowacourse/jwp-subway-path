package subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Subway {

    private final List<Line> lines;

    public Subway() {
        this(new ArrayList<>());
    }

    public Subway(final List<Line> lines) {
        this.lines = lines;
    }

    public void registerLine(final Line line) {
        validate(line);
        lines.add(line);
    }

    private void validate(final Line line) {
        if (isDuplicatedName(line)) {
            throw new IllegalArgumentException("중복되는 이름의 노선이 이미 존재합니다.");
        }
    }

    private boolean isDuplicatedName(final Line line) {
        return lines.stream()
                .anyMatch(it -> it.hasName(line.getName()));
    }

    public void registerSection(
            final String name,
            final Station source,
            final Station target,
            final int distance
    ) {
        final Line line = getLineByName(name);
        line.registerSection(source, target, distance);
    }

    private Line getLineByName(final String name) {
        return lines.stream()
                .filter(line -> line.hasName(name))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));
    }

    public void deleteStation(final String name, final Station station) {
        final Line line = getLineByName(name);
        line.deleteStation(station);
    }

    public List<Line> getLines() {
        return lines;
    }
}
