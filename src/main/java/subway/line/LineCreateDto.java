package subway.line;

import java.beans.ConstructorProperties;

public class LineCreateDto {

    private final String name;

    @ConstructorProperties(value = "name")
    public LineCreateDto(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
