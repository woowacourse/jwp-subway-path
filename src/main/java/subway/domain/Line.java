package subway.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Line {

    private final Long id;
    private final String name;
    private final String color;
    private final List<InterStation> interStations;

    public Line(final String name, final String color) {
        this(null, name, color);
    }

    public Line(final Long id, final String name, final String color) {
        this(id, name, color, new ArrayList<>());
    }

    public Long getId() {
        return id;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }
}
