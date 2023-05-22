package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.service.domain.Direction;
import subway.service.domain.Distance;
import subway.service.domain.LineProperty;
import subway.service.domain.Path;
import subway.service.domain.RouteMapInLine;
import subway.service.domain.Station;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class RouteMapTest {

    @Test
    @DisplayName("현재 가지고 있는 Map 을 이용하여 노선에 존재하는 Station 을 하행 -> 상행 순서로 반환한다.")
    void getSingleLine_notEmpty() {
        Map<Station, List<Path>> map = new HashMap<>();
        Distance IGNORED_DISTANCE = Distance.from(10);
        LineProperty IGNORED_LINE_PROPERTY = new LineProperty(1L, "1", "1");
        Station previousStation = new Station("previous");
        Station nextStation = new Station("next");
        map.put(previousStation, List.of(new Path(Direction.UP, IGNORED_LINE_PROPERTY, nextStation, IGNORED_DISTANCE)));
        map.put(nextStation, List.of(new Path(Direction.DOWN, IGNORED_LINE_PROPERTY, previousStation, IGNORED_DISTANCE)));
        RouteMapInLine routeMap = new RouteMapInLine(map);

        List<Station> stations = routeMap.getStationsOnLine();

        assertThat(stations).hasSize(2);
        assertThat(stations.get(0)).isEqualTo(previousStation);
        assertThat(stations.get(1)).isEqualTo(nextStation);
    }

    @Test
    @DisplayName("현재 가지고 있는 Map 이 비어있는 경우 빈 리스트를 반환한다.")
    void getSingleLine_empty() {
        RouteMapInLine routeMap = new RouteMapInLine(new HashMap<>());

        List<Station> stations = routeMap.getStationsOnLine();

        assertThat(stations).isEmpty();
    }

}
