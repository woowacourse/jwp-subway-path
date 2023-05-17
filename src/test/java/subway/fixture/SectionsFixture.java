package subway.fixture;

import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.fixture.StationFixture.A;
import subway.fixture.StationFixture.B;
import subway.fixture.StationFixture.C;

public class SectionsFixture {

    public static class AB {
        public static final Distance distance = new Distance(6);
        public static final Section section = new Section(A.stationA, B.stationB, distance);

        public static final Sections sections = Sections.emptySections().addSection(section);
    }

    public static class ABC {
        public static final Distance distance = new Distance(6);
        public static final Section section = new Section(B.stationB, C.stationC, distance);

        public static final Sections sections = AB.sections.addSection(section);
    }
}
