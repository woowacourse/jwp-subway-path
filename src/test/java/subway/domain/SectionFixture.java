package subway.domain;

public class SectionFixture {

    public static final Section SECTION_START = new Section(
            LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_1,
            StationFixture.FIXTURE_STATION_2, new Distance(10));

    public static final Section SECTION_MIDDLE_1 = new Section(
            LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_2,
            StationFixture.FIXTURE_STATION_3, new Distance(10));

    public static final Section SECTION_MIDDLE_2 = new Section(
            LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_3,
            StationFixture.FIXTURE_STATION_4, new Distance(10));

    public static final Section SECTION_MIDDLE_3 = new Section(
            LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_4,
            StationFixture.FIXTURE_STATION_5, new Distance(10));

    public static final Section SECTION_END = new Section(
            LineFixture.FIXTURE_LINE_1, StationFixture.FIXTURE_STATION_5,
            StationFixture.FIXTURE_STATION_6, new Distance(10));

}
