package subway.fixture;

import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.fixture.StationFixture.STATION_A;
import subway.fixture.StationFixture.STATION_B;
import subway.fixture.StationFixture.STATION_C;

public class SectionsFixture {

    public static class SECTION_A_B {
        public static final Distance distance = new Distance(6);
        public static final Section section = new Section(1L, STATION_A.stationA, STATION_B.stationB, distance);

        public static final Sections sections = Sections.emptySections().addSection(section);
    }

    public static class SECTION_A_B_C {
        public static final Distance distance = new Distance(6);
        public static final Section section = new Section(2L, STATION_B.stationB, STATION_C.stationC, distance);

        public static final Sections sections = SECTION_A_B.sections.addSection(section);
    }
}
