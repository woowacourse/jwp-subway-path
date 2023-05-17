package subway.service.domain;

import java.util.List;
import java.util.Map;

public class RouteMap {

    private final Map<Station, List<Path>> map;

    public RouteMap(Map<Station, List<Path>> map) {
        this.map = map;
    }

    public Map<Station, List<Path>> getMap() {
        return map;
    }

}
