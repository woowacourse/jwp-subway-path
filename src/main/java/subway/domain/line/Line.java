package subway.domain.line;

import java.util.List;
import subway.domain.section.Sections;

public class Line {

    private static final int EMPTY = 0;
    private static final int NOT_EXIST = 0;

    private Long id;
    private Name name;
    private Color color;
    private Sections sections;

    public Line() {
    }


}
