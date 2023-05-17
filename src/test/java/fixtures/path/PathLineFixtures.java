package fixtures.path;

import subway.domain.line.Line;

public class PathLineFixtures {

    public static class INITIAL_LINE2 {
        public static final String NAME = "2호선";

        public static final Line FIND_LINE = new Line(1L, NAME);
    }

    public static class INITIAL_LINE7 {
        public static final String NAME = "7호선";

        public static final Line FIND_LINE = new Line(2L, NAME);
    }

    public static class INITIAL_LINE3 {
        public static final String NAME = "3호선";

        public static final Line FIND_LINE = new Line(3L, NAME);
    }
}
