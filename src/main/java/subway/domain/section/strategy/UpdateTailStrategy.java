package subway.domain.section.strategy;

import java.util.List;
import subway.domain.Station;
import subway.domain.section.Section;

public class UpdateTailStrategy implements UpdateSectionsStrategy {

    private static final UpdateTailStrategy INSTANCE = new UpdateTailStrategy();

    private UpdateTailStrategy() {
    }

    public static UpdateTailStrategy getInstance() {
        return INSTANCE;
    }

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

    @Override
    public boolean supportAddSection(final List<Section> sections, final Section section) {
        return sections.get(sections.size() - 1).isEqualNextStation(section.getPrevStation());
    }

    @Override
    public boolean supportRemoveStation(final List<Section> sections, final Station station) {
        return sections.get(sections.size() - 1).isEqualNextStation(station);
    }
}
