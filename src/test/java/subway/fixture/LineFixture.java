package subway.fixture;

import java.util.ArrayList;
import subway.adapter.in.web.line.dto.CreateLineRequest;
import subway.adapter.out.persistence.entity.LineEntity;
import subway.domain.Line;

@SuppressWarnings("NonAsciiCharacters")
public class LineFixture {

    public static class 이호선 {

        private static final Long ID = 1L;
        private static final String NAME = "2호선";
        private static final String COLOR = "GREEN";

        public static final Line LINE = new Line(ID, NAME, COLOR, new ArrayList<>());
        public static final LineEntity ENTITY = new LineEntity(ID, NAME, COLOR);
        public static final CreateLineRequest REQUEST = new CreateLineRequest(NAME, COLOR);
    }

}
