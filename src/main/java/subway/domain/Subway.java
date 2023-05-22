package subway.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Subway {
    private final Map<Station, List<Section>> subwayMap;

    private Subway(Map<Station, List<Section>> subwayMap) {
        this.subwayMap = subwayMap;
    }

    public static Subway from(Sections sections) {
        Map<Station, List<Section>> subwayMap = new LinkedHashMap<>();
        for (Section section : sections.getSections()) {
            Station preStation = section.getPreStation();
            Station station = section.getStation();

            subwayMap.putIfAbsent(preStation, new ArrayList<>());
            List<Section> currentSectionsOfPreStation = subwayMap.get(preStation);
            currentSectionsOfPreStation.add(section);

            subwayMap.putIfAbsent(station, new ArrayList<>());
            List<Section> currentSectionOfStation = subwayMap.get(station);
            currentSectionOfStation.add(section);
        }
        return new Subway(subwayMap);
    }

    public Map<Station, List<Section>> getSubwayMap() {
        return subwayMap;
    }
}
