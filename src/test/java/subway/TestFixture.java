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
    public static final Station STATION_F = new Station(14L, "E");

    public static final Line LINE_A = new Line(4L, "99호선", "green");
    public static final Line LINE_B = new Line(5L, "100호선", "red");

    // 그림 참조: https://github.com/woowacourse/jwp-subway-path/assets/39221443/a2270a47-f63f-4955-bcd4-b85a00ae5999
    static {
        LINE_A.add(new Section(STATION_A, STATION_C, 1));
        LINE_A.add(new Section(STATION_C, STATION_D, 1));
        LINE_A.add(new Section(STATION_D, STATION_E, 6));
        LINE_B.add(new Section(STATION_A, STATION_B, 1));
        LINE_B.add(new Section(STATION_B, STATION_C, 1));
        LINE_B.add(new Section(STATION_C, STATION_E, 6));
    }

    public static final List<Section> SHORTEST_PATH_IN_LINE_A_AND_B_STATION_A_TO_E = List.of(
            new Section(STATION_A, STATION_C, 1),
            new Section(STATION_C, STATION_E, 6)
    );
    public static final List<Station> SHORTEST_PATH_STATIONS_IN_LINE_A_AND_B_STATION_A_TO_E = List.of(
            STATION_A, STATION_C, STATION_E
    );

    public static final Line LINE_C = new Line(1L, "101호선", "green");
    public static final Line LINE_D = new Line(1L, "102호선", "red");

    // 그림 참조: https://github.com/woowacourse/jwp-subway-path/assets/39221443/ab649162-2b65-4ace-b69a-d808190272c0
    static {
        LINE_C.add(new Section(STATION_A, STATION_C, 1));
        LINE_C.add(new Section(STATION_C, STATION_D, 3));
        LINE_C.add(new Section(STATION_D, STATION_E, 4));
        LINE_D.add(new Section(STATION_B, STATION_C, 1));
        LINE_D.add(new Section(STATION_C, STATION_D, 1));
    }

    public static final List<Section> SHORTEST_PATH_IN_LINE_C_AND_D_STATION_A_TO_E = List.of(
            new Section(STATION_A, STATION_C, 1),
            new Section(STATION_C, STATION_D, 1),
            new Section(STATION_D, STATION_E, 4)
    );

    // LINE D (B)--(C)--(D)
    // LINE E (A)------(E)
    // 그림 참조: https://github.com/woowacourse/jwp-subway-path/assets/39221443/ffc23350-54d6-4bef-b859-a58c99c367be
    public static final Line LINE_E = new Line(1L, "103호선", "red",
            List.of(new Section(STATION_A, STATION_E, 4))
    );
}
