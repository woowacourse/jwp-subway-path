package subway.domain;

public class SectionFixture {

    // TODO 네이밍에 LINE을 붙이는게 맞을까? 노선 별 구간 목록 픽스쳐가 따로 있어야 하는 거 아닐까?
    public static final Section LINE1_SECTION_ST1_ST2 = new Section(StationFixture.FIXTURE_STATION_1,
            StationFixture.FIXTURE_STATION_2, new Distance(10));
    public static final Section LINE1_SECTION_ST2_ST3 = new Section(StationFixture.FIXTURE_STATION_2,
            StationFixture.FIXTURE_STATION_3, new Distance(10));
    public static final Section LINE1_SECTION_ST3_ST4 = new Section(StationFixture.FIXTURE_STATION_3,
            StationFixture.FIXTURE_STATION_4, new Distance(10));
    public static final Section LINE1_SECTION_ST4_ST5 = new Section(StationFixture.FIXTURE_STATION_4,
            StationFixture.FIXTURE_STATION_5, new Distance(10));
    public static final Section LINE1_SECTION_ST5_ST6 = new Section(StationFixture.FIXTURE_STATION_5,
            StationFixture.FIXTURE_STATION_6, new Distance(10));

    public static final Section LINE2_SECTION_ST7_ST1 = new Section(StationFixture.FIXTURE_STATION_7,
            StationFixture.FIXTURE_STATION_1, new Distance(10));
    public static final Section LINE2_SECTION_ST1_ST8 = new Section(StationFixture.FIXTURE_STATION_1,
            StationFixture.FIXTURE_STATION_8, new Distance(10));
    public static final Section LINE2_SECTION_ST8_ST9 = new Section(StationFixture.FIXTURE_STATION_8,
            StationFixture.FIXTURE_STATION_9, new Distance(10));
    public static final Section LINE2_SECTION_ST9_ST10 = new Section(StationFixture.FIXTURE_STATION_9,
            StationFixture.FIXTURE_STATION_10, new Distance(10));
}
