package subway.fixture;

import subway.domain.Line;
import subway.dto.request.LineRequest;

@SuppressWarnings("NonAsciiCharacters")
public class LineFixture {

    public static class 이호선 {

        public static final Line LINE = new Line(1L, "2호선", "GREEN");
        public static final LineRequest REQUEST = new LineRequest("2호선", "GREEN");
    }

}
