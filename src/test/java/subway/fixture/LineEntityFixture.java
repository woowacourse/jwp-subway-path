package subway.fixture;

import subway.persistence.dao.entity.LineEntity;

public class LineEntityFixture {
    public static LineEntity EIGHT_LINE_ENTITY = new LineEntity(1L, "1호선", "blue");
    public static LineEntity SECOND_LINE_ENTITY = new LineEntity(2L, "2호선", "green");
    public static LineEntity EIGHT_LINE_NO_ID_ENTITY = new LineEntity(null, "1호선", "blue");
    public static LineEntity SECOND_LINE_NO_ID_ENTITY = new LineEntity(null, "2호선", "green");
}
