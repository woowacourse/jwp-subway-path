package subway.fixture;

import subway.dto.request.LineRequest;
import subway.entity.LineEntity;

@SuppressWarnings("NonAsciiCharacters")
public class LineFixture {

    public static class 이호선 {

        public static final LineEntity ENTITY = new LineEntity(1L, "2호선", "GREEN");
        public static final LineRequest REQUEST = new LineRequest("2호선", "GREEN");
    }

}
