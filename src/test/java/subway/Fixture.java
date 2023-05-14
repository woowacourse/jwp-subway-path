package subway;

import subway.domain.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Fixture {
    public static final Station stationA = new Station("A");
    public static final Station stationB = new Station( "B");
    public static final Station stationC = new Station("C");
    public static final Distance distance1 = new Distance(1);
    public static final Distance distance2 = new Distance(2);
    public static final Section sectionAB = new Section(stationA, stationB, distance2);
    public static final Section sectionBC = new Section(stationB, stationC, distance1);
    private static final Sections sections = new Sections(List.of(sectionAB, sectionBC));
    public static final Line line = new Line(1L, "2호선", "green", sections);
}
