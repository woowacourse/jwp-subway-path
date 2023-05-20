package subway.domain.path;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Objects;

import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;

public class SectionWeightedEdge {

    private final Long id;
    private final Station upStation;
    private final Station downStation;
    private final Line line;
    private final int distance;

    public SectionWeightedEdge(Long id, Station upStation, Station downStation, Line line, int distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.line = line;
        this.distance = distance;
    }

    public static List<SectionWeightedEdge> toSectionWeightedEdges(Sections sections) {
        return sections.getSections().stream()
            .map(SectionWeightedEdge::of)
            .collect(toList());
    }

    private static SectionWeightedEdge of(Section section) {
        return new SectionWeightedEdge(section.getId(), section.getUpStation(), section.getDownStation(),
            section.getLine(), section.getDistance());
    }

    public Section toSection() {
        return new Section(id, upStation, downStation, line, distance);
    }

    public Station getSource() {
        return upStation;
    }

    public Station getTarget() {
        return downStation;
    }

    public int getWeight() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        SectionWeightedEdge that = (SectionWeightedEdge)o;

        if (distance != that.distance)
            return false;
        if (!Objects.equals(id, that.id))
            return false;
        if (!Objects.equals(upStation, that.upStation))
            return false;
        if (!Objects.equals(downStation, that.downStation))
            return false;
        return Objects.equals(line, that.line);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (upStation != null ? upStation.hashCode() : 0);
        result = 31 * result + (downStation != null ? downStation.hashCode() : 0);
        result = 31 * result + (line != null ? line.hashCode() : 0);
        result = 31 * result + distance;
        return result;
    }
}
