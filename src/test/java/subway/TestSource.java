package subway;

import java.util.List;

import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;

public class TestSource {

    public static Station jamsil = new Station(1L, "잠실");
    public static Station jangji = new Station(2L, "장지");
    public static Station cheonho = new Station(3L, "천호");
    public static Station mongchon = new Station(4L, "몽촌토성");
    public static Station gangnam = new Station(5L, "강남");
    public static Station kundae = new Station(6L, "건대입구");

    public static Line pink = new Line(1L, "8호선", "pink");
    public static Line green = new Line(2L, "2호선", "green");

    public static Section cheonhoJamsil10 = new Section(cheonho, jamsil, pink, 10);
    public static Section jamsilJangji10 = new Section(jamsil, jangji, pink, 10);
    // 장지 - 10 - 잠실 - 10 - 천호
    public static Sections line8source = new Sections(List.of(cheonhoJamsil10, jamsilJangji10));
    public static Section kundaeJamsil10 = new Section(kundae, jamsil, green, 10);
    public static Section jamsilGangnam10 = new Section(jamsil, gangnam, green, 10);
    // 강남 - 10 - 잠실 - 10 - 건대
    public static Sections line2source = new Sections(List.of(kundaeJamsil10, jamsilGangnam10));
}
