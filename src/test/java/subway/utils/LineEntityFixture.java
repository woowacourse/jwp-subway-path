package subway.utils;

import subway.line.entity.LineEntity;

public class LineEntityFixture {

    public static final LineEntity LINE_NUMBER_TWO_ENTITY = new LineEntity.Builder().id(1L).name("2호선").build();
    public static final LineEntity NO_ID_LINE_NUMBER_TWO_ENTITY = new LineEntity.Builder().name("2호선").build();
}
