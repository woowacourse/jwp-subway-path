package subway.domain;

import java.util.List;
import java.util.Map;

public class SubwayMap {
    private final Map<Station, List<Section>> subwayMap;

    public SubwayMap(final Map<Station, List<Section>> subwayMap) {
        this.subwayMap = subwayMap;
    }

    public Map<Station, List<Section>> getSubwayMap() {
        return subwayMap;
    }

    public List<Section> findSectionByStation(final Station station) {
        return subwayMap.get(station);
    }
}
