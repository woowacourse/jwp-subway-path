package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class LineRouteMapTest {

    @Test
    @DisplayName("역이 없는 경우를 조회할 수 있다")
    void getRouteMap_emptyCase() {
        //given
        final List<Section> sections = Collections.emptyList();
        final LineRouteMap lineRouteMap = new LineRouteMap(sections);

        //when
        final List<Station> stations = lineRouteMap.getLineStations();

        //then
        assertThat(stations).isEqualTo(Collections.emptyList());
    }

    @Test
    @DisplayName("하나로 이어지지 않는 노선은 생성시 예외를 던진다.")
    void getRouteMap_exception_unlinkedLine() {
        //given
        final List<Section> sections = List.of(
                new Section(new Station("A"), new Station("B"), new Distance(1)),
                new Section(new Station("D"), new Station("E"), new Distance(1))
        );

        //when, then
        assertThatThrownBy(() -> new LineRouteMap(sections))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("순환 노선일 경우 생성시 예외를 던진다")
    void getRouteMap_exception_innerCircle() {
        //given
        final List<Section> sections = List.of(
                new Section(new Station("A"), new Station("B"), new Distance(1)),
                new Section(new Station("B"), new Station("C"), new Distance(1)),
                new Section(new Station("C"), new Station("A"), new Distance(1))
        );

        //when, then
        assertThatThrownBy(() -> new LineRouteMap(sections))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("노선은 정상적으로 생성될 수 있다")
    void getRouteMap() {
        //given
        final List<Section> sections = List.of(
                new Section(new Station("A"), new Station("B"), new Distance(1)),
                new Section(new Station("B"), new Station("C"), new Distance(1))
        );

        //when
        final List<Station> routeMap = new LineRouteMap(sections).getLineStations();

        //then
        assertThat(routeMap).containsExactly(
                new Station("A"),
                new Station("B"),
                new Station("C")
        );
    }
}
