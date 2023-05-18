package subway.fixture;

import subway.domain.line.Line;
import subway.domain.line.LineColor;
import subway.domain.line.LineName;
import subway.entity.LineEntity;

public class LineFixture {

    public static class Line1 {
        public static final LineName name = new LineName("1호선");
        public static final LineColor color = new LineColor("파랑");
        public static final Line line = new Line(name, color);
        public static final LineEntity entity = LineEntity.from(line);
    }

    public static class Line2 {
        public static final LineName name = new LineName("2호선");
        public static final LineColor color = new LineColor("초록");
        public static final Line line = new Line(name, color);
        public static final LineEntity entity = LineEntity.from(line);
    }
}
