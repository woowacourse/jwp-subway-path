package subway.domain;

import java.util.Map;

public class Subway {

    private final Map<Section, Long> sections;

    public Subway(final Map<Section, Long> sections) {
        this.sections = sections;
    }
}
