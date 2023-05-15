package subway.domain.section.strategy;

import java.util.List;
import subway.domain.Section;
import subway.domain.Station;

public class UpdateHeadStrategy implements UpdateSectionsStrategy {

    @Override
    public List<Section> addSection(final List<Section> sections, final Section section) {
        sections.add(0, section);
        return sections;
    }

    @Override
    public List<Section> removeStation(final List<Section> sections, final Station station) {
        sections.remove(0);
        return sections;
    }
}
