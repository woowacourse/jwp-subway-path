package subway.domain.station;

import java.util.LinkedList;
import java.util.List;

public class Sections {
    private final List<Section> sections;

    public Sections() {
        this.sections = new LinkedList<>();
    }

    public void addInitialStations(final Section section) {
        if (!sections.isEmpty()) {
            throw new IllegalStateException();
        }

        this.sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }
}
