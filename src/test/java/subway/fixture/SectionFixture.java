package subway.fixture;

import static subway.fixture.DistanceFixture.DISTANCE_10;
import static subway.fixture.DistanceFixture.DISTANCE_2;
import static subway.fixture.DistanceFixture.DISTANCE_3;
import static subway.fixture.DistanceFixture.DISTANCE_5;
import static subway.fixture.StationFixture.STATION_강남;
import static subway.fixture.StationFixture.STATION_길동;
import static subway.fixture.StationFixture.STATION_몽촌토성;
import static subway.fixture.StationFixture.STATION_암사;
import static subway.fixture.StationFixture.STATION_잠실;

import subway.domain.Section;

public class SectionFixture {
    public static final Section SECTION_강남_잠실_5 = Section.of(STATION_강남, STATION_잠실, DISTANCE_5);

    public static final Section SECTION_길동_강남_5 = Section.of(STATION_길동, STATION_강남, DISTANCE_5);

    public static final Section SECTION_잠실_강남_5 = Section.of(STATION_잠실, STATION_강남, DISTANCE_5);

    public static final Section SECTION_잠실_몽촌토성_5 = Section.of(STATION_잠실, STATION_몽촌토성, DISTANCE_5);

    public static final Section SECTION_잠실_길동_10 = Section.of(STATION_잠실, STATION_길동, DISTANCE_10);

    public static final Section SECTION_강남_암사_5 = Section.of(STATION_강남, STATION_암사, DISTANCE_5);

    public static final Section SECTION_몽촌토성_암사_5 = Section.of(STATION_몽촌토성, STATION_암사, DISTANCE_5);

    public static final Section SECTION_길동_암사_3 = Section.of(STATION_길동, STATION_암사, DISTANCE_3);

    public static final Section SECTION_몽촌토성_길동_2 = Section.of(STATION_몽촌토성, STATION_길동, DISTANCE_2);

    public static final Section SECTION_암사_길동_10 = Section.of(STATION_암사, STATION_길동, DISTANCE_10);
}
