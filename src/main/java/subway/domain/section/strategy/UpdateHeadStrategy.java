package subway.domain.section.strategy;

import java.util.List;
import subway.domain.Station;
import subway.domain.section.Section;

public class UpdateHeadStrategy implements UpdateSectionsStrategy {

    private static final UpdateHeadStrategy INSTANCE = new UpdateHeadStrategy();

    private UpdateHeadStrategy() {
    }

    public static UpdateHeadStrategy getInstance() {
        return INSTANCE;
    }

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

    @Override
    public boolean supportAddSection(final List<Section> sections, final Section section) {
        return sections.get(0).isEqualPrevStation(section.getNextStation());
    }

    @Override
    public boolean supportRemoveStation(final List<Section> sections, final Station station) {
        return sections.get(0).isEqualPrevStation(station);
    }
}
