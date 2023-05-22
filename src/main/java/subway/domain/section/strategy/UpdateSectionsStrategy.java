package subway.domain.section.strategy;

import java.util.List;
import subway.domain.Station;
import subway.domain.section.Section;

public interface UpdateSectionsStrategy {

    List<Section> addSection(final List<Section> sections, final Section section);

    List<Section> removeStation(final List<Section> sections, final Station station);

    boolean supportAddSection(final List<Section> sections, final Section section);

    boolean supportRemoveStation(final List<Section> sections, final Station station);
}
