package subway.fixture;

import subway.domain.*;
import subway.dto.LineResponse;

import java.util.List;

import static subway.fixture.StationFixture.*;
import static subway.fixture.SectionFixture.*;

public class LineFixture {

    public static final LineResponse 호선2_응답 = new LineResponse(1L, "2호선", "초록");
    public static final LineResponse 호선8_응답 = new LineResponse(2L, "8호선", "파랑");

    public static final List<Section> SECTION_LIST1 = List.of(잠실새내_잠실, 잠실_잠실나루);
    public static final List<Section> SECTION_LIST2 = List.of(몽촌토성_잠실나루, 잠실나루_석촌);

    public static final Line 호선2 = new Line(1L, new LineName("2호선"), new LineColor("초록"), new Sections(SECTION_LIST1));
    public static final Line 호선8 = new Line(2L, new LineName("8호선"), new LineColor("파랑"), new Sections(SECTION_LIST2));
    public static final List<Line> LINES = List.of(호선2, 호선8);
}
