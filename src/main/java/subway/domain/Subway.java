package subway.domain;

import java.util.List;
import java.util.Map;

public class Subway {
    private final Map<Station, List<Section>> subway;

    public Subway(Map<Station, List<Section>> subway) {
        this.subway = subway;
    }
}
