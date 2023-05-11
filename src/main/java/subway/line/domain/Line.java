package subway.line.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Line {

    private Long id;
    private String name;
    private String color;

    public Line(final String name, final String color) {
        this.name = name;
        this.color = color;
    }
}
