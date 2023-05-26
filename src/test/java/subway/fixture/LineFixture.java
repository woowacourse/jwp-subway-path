package subway.fixture;

import subway.domain.line.Line;
import subway.domain.line.LineColor;
import subway.domain.line.LineName;
import subway.entity.LineEntity;
import subway.fixture.SectionsFixture.SECTION_A_B;
import subway.fixture.SectionsFixture.SECTION_A_B_C;

public class LineFixture {

    public static class Line1 {
        public static final LineName name = new LineName("1호선");
        public static final LineColor color = new LineColor("파랑");
        public static final Line line = new Line(1L, name, color, SECTION_A_B.sections);
        public static final LineEntity entity = LineEntity.from(line);
    }

    public static class Line2 {
        public static final LineName name = new LineName("2호선");
        public static final LineColor color = new LineColor("초록");
        public static final Line line = new Line(2L, name, color, SECTION_A_B_C.sections);
        public static final LineEntity entity = LineEntity.from(line);
    }
}
