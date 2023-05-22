package subway.domain.route;

import subway.common.exception.SubwayIllegalArgumentException;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;

public class RouteSection {

    private final Line line;
    private final Section section;

    public RouteSection(final Line line, final Section section) {
        validate(line, section);
        this.line = line;
        this.section = section;
    }

    private void validate(final Line line, final Section section) {
        if (!line.containsSection(section)) {
            throw new SubwayIllegalArgumentException("해당 노선의 구간이 아닙니다.");
        }
    }

    public int getDistance() {
        return section.getDistance();
    }

    public Station getUpStation() {
        return section.getUpStation();
    }

    public Station getDownStation() {
        return section.getDownStation();
    }

    public Line getLine() {
        return line;
    }

    public Section getSection() {
        return section;
    }
}
