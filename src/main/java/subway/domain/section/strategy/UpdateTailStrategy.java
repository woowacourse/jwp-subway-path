package subway.domain.section.strategy;

import java.util.List;
import subway.domain.Section;
import subway.domain.Station;

public class UpdateTailStrategy implements UpdateSectionsStrategy {

    @Override
    public List<Section> addSection(final List<Section> sections, final Section section) {
        sections.add(section);
        return sections;
    }

    @Override
    public List<Section> removeStation(final List<Section> sections, final Station station) {
        sections.remove(sections.size() - 1);
        return sections;
    }
}
