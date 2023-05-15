package subway.fixture;

import subway.domain.Section;

import static subway.fixture.LineFixture.LINE;
import static subway.fixture.StationFixture.*;

public class SectionFixture {
    public static final Section SECTION_1 = new Section(LINE, STATION_1, STATION_2, 10L);
    public static final Section SECTION_2 = new Section(LINE, STATION_2, STATION_3, 10L);
}
