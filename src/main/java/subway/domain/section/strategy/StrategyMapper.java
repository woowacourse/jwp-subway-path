package subway.domain.section.strategy;

import java.util.List;
import subway.domain.Section;
import subway.domain.Station;

public class StrategyMapper {

    private static final UpdateHeadStrategy UPDATE_HEAD_STRATEGY = new UpdateHeadStrategy();
    private static final UpdateTailStrategy UPDATE_TAIL_STRATEGY = new UpdateTailStrategy();
    private static final UpdateMiddleStrategy UPDATE_MIDDLE_STRATEGY = new UpdateMiddleStrategy();

    public UpdateSectionsStrategy findStrategy(final List<Section> sections, final Section section) {
        if (sections.get(0).isEqualPrevStation(section.getNextStation())) {
            return UPDATE_HEAD_STRATEGY;
        }
        if (sections.get(sections.size() - 1).isEqualNextStation(section.getPrevStation())) {
            return UPDATE_TAIL_STRATEGY;
        }
        return UPDATE_MIDDLE_STRATEGY;
    }

    public UpdateSectionsStrategy findStrategy(final List<Section> sections, final Station station) {
        if (sections.get(0).isEqualPrevStation(station)) {
            return UPDATE_HEAD_STRATEGY;
        }
        if (sections.get(sections.size() - 1).isEqualNextStation(station)) {
            return UPDATE_TAIL_STRATEGY;
        }
        return UPDATE_MIDDLE_STRATEGY;
    }
}
