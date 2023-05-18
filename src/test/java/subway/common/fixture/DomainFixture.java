package subway.common.fixture;

import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class DomainFixture {

    public static final Station 후추 = new Station(1L, "후추");
    public static final Station 디노 = new Station(2L, "디노");
    public static final Station 조앤 = new Station(3L, "조앤");
    public static final Station 로운 = new Station(4L, "로운");
    public static final Station 침착맨 = new Station(5L, "침착맨");
    public static final Station 주호민 = new Station(5L, "주호민");
    public static final Station 우원박 = new Station(5L, "우원박");

    public static final Section 후추_디노 = new Section(new Station(1L, "후추"), new Station(2L, "디노"), 7);
    public static final Section 디노_조앤 = new Section(new Station(2L, "디노"), new Station(3L, "조앤"), 4);
    public static final Section 조앤_로운 = new Section(new Station(3L, "조앤"), new Station(4L, "로운"), 5);

    public static final Line 일호선_남색_후추_디노_조앤_주호민 = new Line(1L, "일호선", "남색",
            List.of(new Section(후추, 디노, 7),
                    new Section(디노, 조앤, 4),
                    new Section(조앤, 주호민, 5)
            ));
    public static final Line 이호선_초록색_침착맨_디노_로운_우원박_후추 = new Line(1L, "이호선", "초록색",
            List.of(new Section(침착맨, 디노, 5),
                    new Section(디노, 로운, 2),
                    new Section(로운, 우원박, 4),
                    new Section(우원박, 후추, 1)
            ));
}
