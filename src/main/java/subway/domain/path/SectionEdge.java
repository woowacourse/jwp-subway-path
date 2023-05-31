package subway.domain.path;

import org.jgrapht.graph.DefaultWeightedEdge;
import subway.domain.line.Line;
import subway.domain.section.Section;

import java.util.Objects;

public class SectionEdge extends DefaultWeightedEdge {

    private final Section section;

    public SectionEdge(Section section) {
        this.section = section;
    }

    public Section getSection() {
        return section;
    }

    public Line getLine() {
        return section.getLine();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SectionEdge that = (SectionEdge) o;
        return Objects.equals(section, that.section);
    }

    @Override
    public int hashCode() {
        return Objects.hash(section);
    }

    @Override
    public String toString() {
        return "SectionEdge{" +
                "section=" + section +
                '}';
    }
}
