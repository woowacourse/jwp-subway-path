package subway.domain;

public class Line {

    private Long id;
    private String name;
    private Section section;

    public Line(final String name, final Section section) {
        this.name = name;
        this.section = section;
    }
}

/**
 * //for문을 통해서 start를 찾음
 * <p>
 * <p>
 * // C
 * C = 기존section.start;
 * <p>
 * // D
 * D = 기존section.end;
 * <p>
 * 기존.end = G;
 * 새로운.start = G;
 * 새로운.end = D;
 * 기존.start = C;
 */
