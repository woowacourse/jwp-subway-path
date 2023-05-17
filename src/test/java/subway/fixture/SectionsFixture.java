package subway.fixture;

import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.StationName;

public class SectionsFixture {

    public static class AB {
        public static final StationName stationNameA = new StationName("A");
        public static final StationName stationNameB = new StationName("B");

        public static final Station stationA = new Station(stationNameA);
        public static final Station stationB = new Station(stationNameB);

        public static final Distance distance = new Distance(6);
        public static final Section section = new Section(stationA, stationB, distance);

        public static final Sections sections = Sections.emptySections().addSection(section);
    }

    public static class ABC {
        public static final StationName stationNameC = new StationName("C");

        public static final Station stationC = new Station(stationNameC);

        public static final Distance distance = new Distance(6);
        public static final Section section = new Section(AB.stationB, stationC, distance);

        public static final Sections sections = AB.sections.addSection(section);
    }
}
