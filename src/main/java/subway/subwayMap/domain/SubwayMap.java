package subway.subwayMap.domain;

import org.springframework.stereotype.Component;
import subway.line.domain.Line;
import subway.station.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 지하철 지도 서비스에서 가장 중요한 서비스는 검색
 * 검색마다 query 를 날리는것은 비효율 적이기 때문에 Bean으로 관리
 */
@Component
public class SubwayMap {

    private final Map<Line, List<Station>> subwayMaps;

    public SubwayMap() {
        this.subwayMaps = new ConcurrentHashMap<>();
    }

    public void put(final Line line, final List<Station> sections) {
        subwayMaps.put(line, sections);
    }

    public List<Station> getSubwayMapByLine(final Line line) {
        return subwayMaps.getOrDefault(line, new ArrayList<>());
    }

    public void addStation(Line line,Station station1,Station station2,boolean isDirection){
        List<Station> stations = subwayMaps.get(line);
        if (isDirection){
            int index = stations.indexOf(station1);
            stations.add(index+1,station2);
            return;
        }
        int index = stations.indexOf(station1);
        stations.add(index,station2);
    }

    public void deleteStation(Line line,Station station){
        List<Station> stations = subwayMaps.get(line);

        stations.remove(station);
    }
}
