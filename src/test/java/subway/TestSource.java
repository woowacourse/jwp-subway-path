package subway;

import java.util.List;

import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

public class TestSource {

    public static Station jamsil = new Station(1L, "잠실");
    public static Station jangji = new Station(2L, "장지");
    public static Station cheonho = new Station(3L, "천호");
    public static Station mongchon = new Station(4L, "몽촌토성");
    public static Station gangnam = new Station(5L, "강남");
    public static Station kundae = new Station(6L, "건대입구");
    public static Station wangsimni = new Station(7L, "왕십리");

    public static Line pink = new Line(1L, "8호선", "pink", 0);
    public static Line green = new Line(2L, "2호선", "green", 0);
    public static Line purple = new Line(3L, "5호선", "purple", 0);

    public static Section cheonhoJamsil10 = new Section(1L, cheonho, jamsil, pink, 10);
    public static Section jamsilJangji10 = new Section(2L, jamsil, jangji, pink, 10);
    public static Section kundaeJamsil10 = new Section(3L, kundae, jamsil, green, 10);
    public static Section jamsilGangnam10 = new Section(4L, jamsil, gangnam, green, 10);

    // 장지 - 10 - 잠실 - 10 - 천호
    public static List<Section> line8Sections = List.of(cheonhoJamsil10, jamsilJangji10);
}
