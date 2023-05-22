package subway;

import java.util.List;

import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

@SuppressWarnings("NonAsciiCharacters")
public class TestFixture {

    public static final Long JAMSILNARU_ID = 1L;
    public static final Long JAMSIL_ID = 2L;
    public static final Long JAMSILSAENAE_ID = 3L;
    public static final Long JEONGJA_ID = 4L;
    public static final Long PANKYO_ID = 5L;
    public static final Long 종합운동장역_ID = 6L;
    public static final Long 몽촌토성역_ID = 7L;
    public static final Long 석촌역_ID = 8L;

    public static final Station 잠실나루역 = new Station(JAMSILNARU_ID, "잠실나루");
    public static final Station 잠실역 = new Station(JAMSIL_ID, "잠실");
    public static final Station 잠실새내역 = new Station(JAMSILSAENAE_ID, "잠실새내");
    public static final Station 종합운동장역 = new Station(종합운동장역_ID, "종합운동장");

    public static final Station 몽촌토성역 = new Station(몽촌토성역_ID, "몽촌토성");
    public static final Station 석촌역 = new Station(석촌역_ID, "석촌역");

    public static final Section 잠실나루역_잠실역 = new Section(잠실나루역, 잠실역, 10);
    public static final Section 잠실역_잠실새내역 = new Section(잠실역, 잠실새내역, 5);
    public static final Section 잠실새내역_종합운동장역 = new Section(잠실새내역, 종합운동장역, 5);

    public static final Section 몽촌토성역_잠실역 = new Section(몽촌토성역, 잠실역, 5);
    public static final Section 잠실역_석촌역 = new Section(잠실역, 석촌역, 5);

    public static final Station STATION_A = new Station(9L, "A");
    public static final Station STATION_B = new Station(10L, "B");
    public static final Station STATION_C = new Station(11L, "C");
    public static final Station STATION_D = new Station(12L, "D");
    public static final Station STATION_E = new Station(13L, "E");
}
