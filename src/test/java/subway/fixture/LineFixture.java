package subway.fixture;

import subway.domain.Line;
import subway.dto.request.LineRequest;
import subway.entity.LineEntity;

import java.util.ArrayList;

@SuppressWarnings("NonAsciiCharacters")
public class LineFixture {

    public static class 이호선 {

        private static final Long ID = 1L;
        private static final String NAME = "2호선";
        private static final String COLOR = "GREEN";

        public static final Line LINE = new Line(ID, NAME, COLOR, new ArrayList<>());
        public static final LineEntity ENTITY = new LineEntity(ID, NAME, COLOR);
        public static final LineRequest REQUEST = new LineRequest(NAME, COLOR);
    }

    public static class 신분당선 {

        private static final Long ID = 2L;
        private static final String NAME = "신분당선";
        private static final String COLOR = "RED";

        public static final Line LINE = new Line(ID, NAME, COLOR, new ArrayList<>());
        public static final LineEntity ENTITY = new LineEntity(ID, NAME, COLOR);
        public static final LineRequest REQUEST = new LineRequest(NAME, COLOR);
    }

    public static class 구호선 {

        private static final Long ID = 1L;
        private static final String NAME = "9호선";
        private static final String COLOR = "BROWN";

        public static final Line LINE = new Line(ID, NAME, COLOR, new ArrayList<>());
        public static final LineEntity ENTITY = new LineEntity(ID, NAME, COLOR);
        public static final LineRequest REQUEST = new LineRequest(NAME, COLOR);
    }

}
