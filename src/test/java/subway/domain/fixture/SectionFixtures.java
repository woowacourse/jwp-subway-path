package subway.domain.fixture;

import subway.domain.Section;
import subway.domain.Station;

public class SectionFixtures {

    public static Section createSection(final String upStationName, final String downStationName, final int distance) {
        return new Section(new Station(upStationName), new Station(downStationName), distance);
    }
}
