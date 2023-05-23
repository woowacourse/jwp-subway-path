package subway.domain.route;

import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.vo.Distance;

import java.util.Objects;

public class RouteEdge {

    private final Section section;
    private final Line line;

    public RouteEdge(final Section section, final Line line) {
        this.section = section;
        this.line = line;
    }

    public static RouteEdge from(final Section section, final Line line) {
        return new RouteEdge(section, line);
    }

    public Section getSection() {
        return section;
    }

    public Station getUpStation() {
        return section.getUpStation();
    }

    public Station getDownStation() {
        return section.getDownStation();
    }

    public Distance getDistance() {
        return section.getDistance();
    }

    public Line getLine() {
        return line;
    }

    public boolean isNotSameLine(final RouteEdge otherRouteEdge) {
        return !line.equals(otherRouteEdge.line);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final RouteEdge routeEdge = (RouteEdge) o;
        return Objects.equals(section, routeEdge.section) && Objects.equals(line, routeEdge.line);
    }

    @Override
    public int hashCode() {
        return Objects.hash(section, line);
    }
}
