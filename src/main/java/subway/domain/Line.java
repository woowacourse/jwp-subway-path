package subway.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Line {
    private Long id;
    private String name;

    public Line(final String name) {
        this.name = name;
    }
}
