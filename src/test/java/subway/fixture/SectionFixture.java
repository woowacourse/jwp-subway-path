package subway.fixture;

import subway.domain.Section;

import static subway.fixture.LineFixture.*;
import static subway.fixture.StationFixture.*;

public class SectionFixture {
    public static final Section SECTION_1 = new Section(LINE_1, STATION_1, STATION_2, 5L);
    public static final Section SECTION_2 = new Section(LINE_1, STATION_2, STATION_3, 8L);
    public static final Section SECTION_3 = new Section(LINE_1, STATION_3, STATION_4, 10L);
    public static final Section SECTION_4 = new Section(LINE_1, STATION_4, STATION_5, 7L);
    public static final Section SECTION_5 = new Section(LINE_1, STATION_5, STATION_6, 10L);
    public static final Section SECTION_6 = new Section(LINE_1, STATION_6, STATION_7, 1L);
    public static final Section SECTION_7 = new Section(LINE_2, STATION_5, STATION_8, 10L);
    public static final Section SECTION_8 = new Section(LINE_2, STATION_8, STATION_9, 15L);
    public static final Section SECTION_9 = new Section(LINE_2, STATION_9, STATION_10, 12L);
    public static final Section SECTION_10 = new Section(LINE_2, STATION_10, STATION_11, 10L);
    public static final Section SECTION_11 = new Section(LINE_2, STATION_11, STATION_12, 3L);
    public static final Section SECTION_12 = new Section(LINE_3, STATION_12, STATION_13, 2L);
    public static final Section SECTION_13 = new Section(LINE_3, STATION_13, STATION_14, 7L);
    public static final Section SECTION_14 = new Section(LINE_3, STATION_14, STATION_15, 10L);
    public static final Section SECTION_15 = new Section(LINE_3, STATION_15, STATION_19, 1L);
    public static final Section SECTION_16 = new Section(LINE_3, STATION_19, STATION_18, 10L);
    public static final Section SECTION_17 = new Section(LINE_3, STATION_18, STATION_17, 9L);
    public static final Section SECTION_18 = new Section(LINE_3, STATION_17, STATION_16, 2L);
    public static final Section SECTION_19 = new Section(LINE_3, STATION_16, STATION_7, 10L);
}
