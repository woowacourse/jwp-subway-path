package subway.fixture;

import subway.domain.Color;
import subway.domain.Name;
import subway.domain.Section;
import subway.domain.Station;

@SuppressWarnings("NonAsciiCharacters")
public class Fixture {

    public static final Station 후추 = new Station(1L, "후추");
    public static final Station 디노 = new Station(2L, "디노");
    public static final Station 조앤 = new Station(3L, "조앤");
    public static final Station 로운 = new Station(4L, "로운");

    public static final Section 후추_디노 = new Section(후추, 디노, 7);
    public static final Section 디노_조앤 = new Section(디노, 조앤, 4);
    public static final Section 조앤_로운 = new Section(조앤, 로운, 5);

    public static final Name 일호선 = new Name("일호선");
    public static final Name 이호선 = new Name("이호선");

    public static final Color 남색 = new Color("남색");
    public static final Color 초록색 = new Color("초록색");


}
