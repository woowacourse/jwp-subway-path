package subway.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Lines {

    private final List<Line> value;

    public Lines(final List<Line> value) {
        this.value = value;
    }

    public List<Section> findAllSections() {
        return value.stream()
            .flatMap(line -> line.getAllSection().stream())
            .collect(Collectors.toList());
    }
}
