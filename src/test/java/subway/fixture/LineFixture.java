package subway.fixture;

import subway.service.line.domain.Line;

public class LineFixture {
    public static Line EIGHT_LINE = new Line(1L, "1호선", "blue");
    public static Line SECOND_LINE = new Line(2L, "2호선", "green");
    public static Line EIGHT_LINE_NO_ID = new Line(null, "1호선", "blue");
    public static Line SECOND_LINE_NO_ID = new Line(null, "2호선", "green");
}
