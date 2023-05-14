package subway.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LineEntity {
    private Long id;
    private String name;
    private String color;

    public LineEntity(String name, String color) {
        this(null, name, color);
    }
}
