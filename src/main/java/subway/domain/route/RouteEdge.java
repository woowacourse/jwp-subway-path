package subway.domain.route;

import subway.domain.vo.Distance;
import subway.domain.vo.Name;
import subway.domain.Section;
import subway.domain.Station;

import java.util.Objects;

public class RouteEdge {

    private final Section section;
    private final Name lineName;

    public RouteEdge(final Section section, final Name lineName) {
        this.section = section;
        this.lineName = lineName;
    }

    public static RouteEdge from(final Section section, final String lineName) {
        return new RouteEdge(section, new Name(lineName));
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

    public Name getLineName() {
        return lineName;
    }

    @Override
    public boolean equals(final Object o) {
          if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final RouteEdge that = (RouteEdge) o;
        return Objects.equals(section, that.section) && Objects.equals(lineName, that.lineName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(section, lineName);
    }
}
