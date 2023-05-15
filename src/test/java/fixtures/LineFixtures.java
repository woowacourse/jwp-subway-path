package fixtures;

import java.util.List;

import subway.domain.line.Line;
import subway.dto.LineFindResponse;

public class LineFixtures {

    public static class INITIAL_Line2 {
        public static final Long ID = 1L;
        public static final String NAME = "2호선";
        public static final Line FIND_LINE = new Line(ID, NAME);
        public static final LineFindResponse FIND_RESPONSE = new LineFindResponse(NAME, List.of("잠실역", "강변역", "건대역"));
    }

    public static class Line7 {
        public static final String NAME = "7호선";
        public static final Line DUMMY_LINE7 = new Line(100L, NAME);
        public static final Line INSERT_ENTITY = new Line(null, NAME);
        public static final LineFindResponse FIND_RESPONSE = new LineFindResponse(NAME, List.of("온수역", "대림역", "논현역", "장암역"));

    }

    public static final List<LineFindResponse> ALL_Line_FIND_RESPONSE = List.of(INITIAL_Line2.FIND_RESPONSE, Line7.FIND_RESPONSE);
}
