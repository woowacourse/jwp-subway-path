package subway.domain.strategy;

import java.util.List;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;

public interface AddStationStrategy {

    void addStation(
            final List<Section> sections,
            final Station base,
            final Station additional,
            final Distance distance
    );

    default void changeDistance(
            final List<Section> sections,
            final Station start,
            final Station end,
            final Section originSection,
            final Distance distance
    ) {
        sections.add(new Section(start, end, originSection.subtract(distance)));
        sections.remove(originSection);
    }
}
