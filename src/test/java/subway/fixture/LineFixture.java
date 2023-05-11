package subway.fixture;

import java.util.ArrayList;
import subway.domain.Line;
import subway.dto.request.LineRequest;
import subway.entity.LineEntity;

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

}
