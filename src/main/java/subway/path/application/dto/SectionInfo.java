package subway.path.application.dto;

import static java.util.stream.Collectors.toList;

import java.util.List;
import subway.line.domain.Line;
import subway.line.domain.Section;

public class SectionInfo {

    private final String line;
    private final String fromStation;
    private final String toStation;
    private final int distance;

    public SectionInfo(final String line, final String fromStation, final String toStation, final int distance) {
        this.line = line;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.distance = distance;
    }

    public static SectionInfo of(final String line, final Section section) {
        return new SectionInfo(line, section.up().name(), section.down().name(), section.distance());
    }

    public static List<SectionInfo> from(final Line line) {
        return line.sections()
                .stream()
                .map(it -> SectionInfo.of(line.name(), it))
                .collect(toList());
    }

    public String getLine() {
        return line;
    }

    public String getFromStation() {
        return fromStation;
    }

    public String getToStation() {
        return toStation;
    }

    public int getDistance() {
        return distance;
    }
}
