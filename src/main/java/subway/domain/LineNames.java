package subway.domain;

import java.util.List;
import java.util.Objects;

public class LineNames {

    private final List<String> lineNames;

    public LineNames(List<String> lineNames) {
        this.lineNames = lineNames;
    }

    public boolean hasLineOfName(String lineName) {
        return lineNames.stream()
                .anyMatch(name -> Objects.equals(name, lineName));
    }
}
