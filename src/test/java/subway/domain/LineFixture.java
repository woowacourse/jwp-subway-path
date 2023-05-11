package subway.domain;

public class LineFixture {
    public static Line FIRST_LINE = new Line(1L, "1호선", "blue");
    public static Line SECOND_LINE = new Line(2L, "2호선", "green");
    public static Line SECOND_LINE_NO_ID = new Line(null, "2호선", "green");
}
