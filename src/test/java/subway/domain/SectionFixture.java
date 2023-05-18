package subway.domain;

import static subway.domain.StationFixture.FIXTURE_STATION_1;
import static subway.domain.StationFixture.FIXTURE_STATION_10;
import static subway.domain.StationFixture.FIXTURE_STATION_2;
import static subway.domain.StationFixture.FIXTURE_STATION_3;
import static subway.domain.StationFixture.FIXTURE_STATION_4;
import static subway.domain.StationFixture.FIXTURE_STATION_5;
import static subway.domain.StationFixture.FIXTURE_STATION_6;
import static subway.domain.StationFixture.FIXTURE_STATION_7;
import static subway.domain.StationFixture.FIXTURE_STATION_8;
import static subway.domain.StationFixture.FIXTURE_STATION_9;

import java.util.List;

public class SectionFixture {

    public static final Section LINE1_SECTION_ST1_ST2 = new Section(FIXTURE_STATION_1, FIXTURE_STATION_2,
            new Distance(8));
    public static final Section LINE1_SECTION_ST2_ST3 = new Section(FIXTURE_STATION_2,
            FIXTURE_STATION_3, new Distance(10));
    public static final Section LINE1_SECTION_ST3_ST4 = new Section(FIXTURE_STATION_3, FIXTURE_STATION_4,
            new Distance(10));
    public static final Section LINE1_SECTION_ST4_ST5 = new Section(FIXTURE_STATION_4, FIXTURE_STATION_5,
            new Distance(10));
    public static final Section LINE1_SECTION_ST5_ST6 = new Section(FIXTURE_STATION_5, FIXTURE_STATION_6,
            new Distance(10));

    public static final Section LINE2_SECTION_ST7_ST1 = new Section(FIXTURE_STATION_7, FIXTURE_STATION_1,
            new Distance(10));
    public static final Section LINE2_SECTION_ST1_ST8 = new Section(FIXTURE_STATION_1, FIXTURE_STATION_8,
            new Distance(10));
    public static final Section LINE2_SECTION_ST8_ST9 = new Section(FIXTURE_STATION_8, FIXTURE_STATION_9,
            new Distance(10));
    public static final Section LINE2_SECTION_ST9_ST10 = new Section(FIXTURE_STATION_9, FIXTURE_STATION_10,
            new Distance(10));

    public static final Section LINE3_SECTION_ST2_ST9 = new Section(FIXTURE_STATION_2, FIXTURE_STATION_9,
            new Distance(10));

    public static final List<Section> LINE1_SECTIONS = List.of(
            LINE1_SECTION_ST1_ST2,
            LINE1_SECTION_ST2_ST3,
            LINE1_SECTION_ST3_ST4,
            LINE1_SECTION_ST4_ST5,
            LINE1_SECTION_ST5_ST6
    );

    public static final List<Section> LINE2_SECTIONS = List.of(
            LINE2_SECTION_ST7_ST1,
            LINE2_SECTION_ST1_ST8,
            LINE2_SECTION_ST8_ST9,
            LINE2_SECTION_ST9_ST10
    );

    public static final List<Section> LINE3_SECTIONS = List.of(LINE3_SECTION_ST2_ST9);
}
