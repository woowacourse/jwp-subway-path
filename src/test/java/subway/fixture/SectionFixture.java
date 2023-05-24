package subway.fixture;

import static subway.fixture.StationFixture.FIXTURE_STATION_1;
import static subway.fixture.StationFixture.FIXTURE_STATION_10;
import static subway.fixture.StationFixture.FIXTURE_STATION_2;
import static subway.fixture.StationFixture.FIXTURE_STATION_3;
import static subway.fixture.StationFixture.FIXTURE_STATION_4;
import static subway.fixture.StationFixture.FIXTURE_STATION_5;
import static subway.fixture.StationFixture.FIXTURE_STATION_6;
import static subway.fixture.StationFixture.FIXTURE_STATION_7;
import static subway.fixture.StationFixture.FIXTURE_STATION_8;
import static subway.fixture.StationFixture.FIXTURE_STATION_9;

import java.util.List;
import subway.domain.entity.Section;
import subway.domain.vo.Distance;

public class SectionFixture {

    public static final Section SECTION_ST1_ST2 = new Section(FIXTURE_STATION_1, FIXTURE_STATION_2,
            new Distance(8));
    public static final Section SECTION_ST2_ST3 = new Section(FIXTURE_STATION_2,
            FIXTURE_STATION_3, new Distance(10));
    public static final Section SECTION_ST3_ST4 = new Section(FIXTURE_STATION_3, FIXTURE_STATION_4,
            new Distance(10));
    public static final Section SECTION_ST4_ST5 = new Section(FIXTURE_STATION_4, FIXTURE_STATION_5,
            new Distance(10));
    public static final Section SECTION_ST5_ST6 = new Section(FIXTURE_STATION_5, FIXTURE_STATION_6,
            new Distance(10));
    public static final Section SECTION_ST7_ST1 = new Section(FIXTURE_STATION_7, FIXTURE_STATION_1,
            new Distance(10));
    public static final Section SECTION_ST1_ST8 = new Section(FIXTURE_STATION_1, FIXTURE_STATION_8,
            new Distance(10));
    public static final Section SECTION_ST8_ST9 = new Section(FIXTURE_STATION_8, FIXTURE_STATION_9,
            new Distance(10));
    public static final Section SECTION_ST9_ST10 = new Section(FIXTURE_STATION_9, FIXTURE_STATION_10,
            new Distance(10));
    public static final Section SECTION_ST2_ST9 = new Section(FIXTURE_STATION_2, FIXTURE_STATION_9,
            new Distance(10));

    public static final List<Section> ROUTED_SECTIONS_1_TRANSFER_2_AT_ST1 = List.of(
            SECTION_ST1_ST2,
            SECTION_ST2_ST3,
            SECTION_ST3_ST4,
            SECTION_ST4_ST5,
            SECTION_ST5_ST6
    );

    public static final List<Section> ROUTED_SECTIONS_2_TRANSFER_1_AT_ST1_3_AT_ST9 = List.of(
            SECTION_ST7_ST1,
            SECTION_ST1_ST8,
            SECTION_ST8_ST9,
            SECTION_ST9_ST10
    );

    public static final List<Section> ROUTED_SECTIONS_3_TRANSFER_1_AT_ST2_TRANSFER_2_AT_ST9 = List.of(
            SECTION_ST2_ST9
    );
}
