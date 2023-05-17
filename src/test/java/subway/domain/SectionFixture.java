package subway.domain;

public class SectionFixture {

    public static final Section LINE1_SECTION_ST1_ST2 = new Section(StationFixture.FIXTURE_STATION_1,
            StationFixture.FIXTURE_STATION_2, new Distance(10));
    public static final Section LINE1_SECTION_MIDDLE_ST2_ST3 = new Section(StationFixture.FIXTURE_STATION_2,
            StationFixture.FIXTURE_STATION_3, new Distance(10));
    public static final Section LINE1_SECTION_MIDDLE_ST3_ST4 = new Section(StationFixture.FIXTURE_STATION_3,
            StationFixture.FIXTURE_STATION_4, new Distance(10));
    public static final Section LINE1_SECTION_MIDDLE_ST4_ST5 = new Section(StationFixture.FIXTURE_STATION_4,
            StationFixture.FIXTURE_STATION_5, new Distance(10));
    public static final Section LINE1_SECTION_ST5_ST6 = new Section(StationFixture.FIXTURE_STATION_5,
            StationFixture.FIXTURE_STATION_6, new Distance(10));

}
