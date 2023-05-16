package subway.domain;

import java.util.List;

public class Lines {

    private final List<Line> value;

    public Lines(final List<Line> value) {
        this.value = value;
    }

    public List<Line> getAllLine() {
        return List.copyOf(value);
    }
}
