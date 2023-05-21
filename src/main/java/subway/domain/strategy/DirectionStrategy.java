package subway.domain.strategy;

import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;
import subway.exception.InvalidInputException;

import java.util.List;
import java.util.Optional;

public interface DirectionStrategy {
    String UP_VALUE = "UP";
    String DOWN_VALUE = "DOWN";

    static DirectionStrategy from(String directionValue) {
        if (UP_VALUE.equals(directionValue)) {
            return new UpStrategy();
        }

        if (DOWN_VALUE.equals(directionValue)) {
            return new DownStrategy();
        }

        throw new InvalidInputException("잘못된 방향입니다. 방향은 UP 또는 DOWN만 가능합니다.");
    }

    Section createSectionWith(Station baseStation, Station newStation, Distance distance, long lineId);

    Optional<Section> findSection(Station baseStation, List<Section> sections);

    Section createSectionBasedOn(Section originalSection, Section newDistance);
}
