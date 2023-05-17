package subway.domain.section.delete;

import subway.domain.Station;
import subway.domain.section.Sections;
import subway.domain.section.delete.strategy.DeleteStationStrategy;
import subway.domain.section.delete.strategy.EndDeleteStationStrategy;
import subway.domain.section.delete.strategy.MidDeleteStationStrategy;

import java.util.Arrays;
import java.util.function.Predicate;

public enum DeleteStationHandler {
    END(Station::isEndStation, new EndDeleteStationStrategy()),
    MID(Station::isMidStation, new MidDeleteStationStrategy());

    private final Predicate<Station> isStrategy;
    private final DeleteStationStrategy deleteStationStrategy;

    DeleteStationHandler(final Predicate<Station> isStrategy, final DeleteStationStrategy deleteStationStrategy) {
        this.isStrategy = isStrategy;
        this.deleteStationStrategy = deleteStationStrategy;
    }

    public static DeleteStationStrategy bind(final Sections sections, final Station station) {
        final Station removeStation = sections.findStation(station);

        return Arrays.stream(DeleteStationHandler.values())
                .filter(deleteStationHandler -> deleteStationHandler.isStrategy.test(removeStation))
                .map(deleteStationHandler -> deleteStationHandler.deleteStationStrategy)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("역을 제거할 수 없습니다."));
    }
}
