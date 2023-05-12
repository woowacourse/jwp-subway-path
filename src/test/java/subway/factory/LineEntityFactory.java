package subway.factory;

import subway.entity.LineEntity;

public class LineEntityFactory {

    public static LineEntity createLineEntity() {
        return new LineEntity(1L, 2L, "2호선", "green");
    }

    public static LineEntity createLineEntity2() {
        return new LineEntity(2L, 8L, "8호선", "red");
    }
}
