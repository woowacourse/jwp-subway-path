package subway.fixture;

import subway.domain.Distance;
import subway.domain.Section;

import static subway.fixture.LineFixture.*;
import static subway.fixture.StationFixture.*;

public class SectionFixture {
    public static final Section SECTION_1 = new Section(LINE_1, STATION_1, STATION_2, new Distance(5));
    public static final Section SECTION_2 = new Section(LINE_1, STATION_2, STATION_3, new Distance(8));
    public static final Section SECTION_3 = new Section(LINE_1, STATION_3, STATION_4, new Distance(10));
    public static final Section SECTION_4 = new Section(LINE_1, STATION_4, STATION_5, new Distance(7));
    public static final Section SECTION_5 = new Section(LINE_1, STATION_5, STATION_6, new Distance(10));
    public static final Section SECTION_6 = new Section(LINE_1, STATION_6, STATION_7, new Distance(1));
    public static final Section SECTION_7 = new Section(LINE_2, STATION_5, STATION_8, new Distance(10));
    public static final Section SECTION_8 = new Section(LINE_2, STATION_8, STATION_9, new Distance(15));
    public static final Section SECTION_9 = new Section(LINE_2, STATION_9, STATION_10, new Distance(12));
    public static final Section SECTION_10 = new Section(LINE_2, STATION_10, STATION_11, new Distance(10));
    public static final Section SECTION_11 = new Section(LINE_2, STATION_11, STATION_12, new Distance(3));
    public static final Section SECTION_12 = new Section(LINE_3, STATION_12, STATION_13, new Distance(2));
    public static final Section SECTION_13 = new Section(LINE_3, STATION_13, STATION_14, new Distance(7));
    public static final Section SECTION_14 = new Section(LINE_3, STATION_14, STATION_15, new Distance(10));
    public static final Section SECTION_15 = new Section(LINE_3, STATION_15, STATION_19, new Distance(1));
    public static final Section SECTION_16 = new Section(LINE_3, STATION_19, STATION_18, new Distance(10));
    public static final Section SECTION_17 = new Section(LINE_3, STATION_18, STATION_17, new Distance(9));
    public static final Section SECTION_18 = new Section(LINE_3, STATION_17, STATION_16, new Distance(2));
    public static final Section SECTION_19 = new Section(LINE_3, STATION_16, STATION_7, new Distance(10));
}
