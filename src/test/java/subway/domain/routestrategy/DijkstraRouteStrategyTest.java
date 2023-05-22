package subway.domain.routestrategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.Subway;

class DijkstraRouteStrategyTest {
//
//    private static final Sections sectionsOfLine2 = new Sections(List.of(
//            new Section(new Station("낙성대"), new Station("사당"), new Distance(5)),
//            new Section(new Station("사당"), new Station("교대"), new Distance(5)),
//            new Section(new Station("교대"), new Station("방배"), new Distance(5)))
//    );
//    private static final Sections sectionsOfLine3 = new Sections(List.of(
//            new Section(new Station("잠원"), new Station("고속터미널"), new Distance(5)),
//            new Section(new Station("고속터미널"), new Station("교대"), new Distance(500)),
//            new Section(new Station("교대"), new Station("남부터미널"), new Distance(5)))
//    );
//    private static final Sections sectionsOfLine4 = new Sections(List.of(
//            new Section(new Station("사당"), new Station("이수"), new Distance(5)),
//            new Section(new Station("이수"), new Station("동작"), new Distance(5)))
//    );
//    private static final Sections sectionsOfLine5 = new Sections(List.of(
//            new Section(new Station("천호"), new Station("강동"), new Distance(5)))
//    );
//    private static final Sections sectionsOfLine7 = new Sections(List.of(
//            new Section(new Station("이수"), new Station("내방"), new Distance(5)),
//            new Section(new Station("내방"), new Station("고속터미널"), new Distance(5)),
//            new Section(new Station("고속터미널"), new Station("반포"), new Distance(5)))
//    );

    private static final Sections sectionsOfLine2 = new Sections(List.of(
            new Section(new Station(1L, "낙성대"), new Station(2L, "사당"), new Distance(5)),
            new Section(new Station(2L, "사당"), new Station(3L, "교대"), new Distance(5)),
            new Section(new Station(3L, "교대"), new Station(4L, "방배"), new Distance(5)))
    );
    private static final Sections sectionsOfLine3 = new Sections(List.of(
            new Section(new Station(5L, "잠원"), new Station(6L, "고속터미널"), new Distance(5)),
            new Section(new Station(6L, "고속터미널"), new Station(3L, "교대"), new Distance(500)),
            new Section(new Station(3L, "교대"), new Station(8L, "남부터미널"), new Distance(5)))
    );
    private static final Sections sectionsOfLine4 = new Sections(List.of(
            new Section(new Station(2L, "사당"), new Station(10L, "이수"), new Distance(5)),
            new Section(new Station(10L, "이수"), new Station(11L, "동작"), new Distance(5)))
    );
    private static final Sections sectionsOfLine5 = new Sections(List.of(
            new Section(new Station(12L, "천호"), new Station(13L, "강동"), new Distance(5)))
    );
    private static final Sections sectionsOfLine7 = new Sections(List.of(
            new Section(new Station(10L, "이수"), new Station(15L, "내방"), new Distance(5)),
            new Section(new Station(15L, "내방"), new Station(6L, "고속터미널"), new Distance(5)),
            new Section(new Station(6L, "고속터미널"), new Station(16L, "반포"), new Distance(5)))
    );

    private static final Line line2 = new Line(2L, "2호선", "초록", sectionsOfLine2);
    private static final Line line3 = new Line(3L, "3호선", "주황", sectionsOfLine3);
    private static final Line line4 = new Line(4L, "4호선", "하늘", sectionsOfLine4);
    private static final Line line5 = new Line(5L, "5호선", "보라", sectionsOfLine5);
    private static final Line line7 = new Line(7L, "7호선", "올리브", sectionsOfLine7);


    @Test
    @DisplayName("노선에 해당 역이 존재하지 않아, 경로를 조회할 수 없는 경우 예외를 발생한다")
    void findShortestRoute_NotExistStationError() {
        Subway lines = new Subway(List.of(line2, line5));
        Station start = new Station("낙성대");
        Station end = new Station("동인천");
        DijkstraRouteStrategy dijkstraRouteStrategy = new DijkstraRouteStrategy();

        assertThatThrownBy(() -> dijkstraRouteStrategy.findShortestRoute(lines, start, end))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 역이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("노선의 역들이 연결되어있지 않아, 경로를 조회할 수 없는 경우 예외를 발생한다")
    void findShortestRoute_NotConnectedError() {
        Subway lines = new Subway(List.of(line2, line5));
        Station start = new Station("낙성대");
        Station end = new Station("천호");
        DijkstraRouteStrategy dijkstraRouteStrategy = new DijkstraRouteStrategy();

        assertThatThrownBy(() -> dijkstraRouteStrategy.findShortestRoute(lines, start, end))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이동할 수 없는 경로입니다.");
    }

    @Test
    @DisplayName("환승이 필요 없는 경우, 경로를 찾아 반환한다")
    void findShortestRoute_No_Transfer() {
        Subway lines = new Subway(List.of(line2, line3));
        Station start = new Station("낙성대");
        Station end = new Station("방배");
        DijkstraRouteStrategy dijkstraRouteStrategy = new DijkstraRouteStrategy();

        List<Station> expected = Stream.of("낙성대", "사당", "교대","방배")
                .map(Station::new)
                .collect(Collectors.toList());

        assertThat(dijkstraRouteStrategy.findShortestRoute(lines, start, end))
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("1번 환승해야 하는 경우 경로를 찾아 반환한다")
    void findShortestRoute_Transfer_Once() {
        Subway lines = new Subway(List.of(line2, line3));
        Station start = new Station("낙성대");
        Station end = new Station("고속터미널");
        DijkstraRouteStrategy dijkstraRouteStrategy = new DijkstraRouteStrategy();

        List<Station> expected = Stream.of("낙성대", "사당", "교대", "고속터미널")
                .map(Station::new)
                .collect(Collectors.toList());

        assertThat(dijkstraRouteStrategy.findShortestRoute(lines, start, end))
                .isEqualTo(expected);
    }


    @Test
    @DisplayName("2번 환승해야 하는 경우 경로를 찾아 반환한다")
    void findShortestRoute_Transfer_Twice() {
        Subway lines = new Subway(List.of(line2, line3, line4, line7));
        Station start = new Station("낙성대");
        Station end = new Station("고속터미널");
        DijkstraRouteStrategy dijkstraRouteStrategy = new DijkstraRouteStrategy();

        List<Station> expected = Stream.of("낙성대", "사당", "이수", "내방", "고속터미널")
                .map(Station::new)
                .collect(Collectors.toList());

        assertThat(dijkstraRouteStrategy.findShortestRoute(lines, start, end))
                .isEqualTo(expected);
    }


    @Test
    @DisplayName("노선에 해당 역이 존재하지 않아, 최단 거리를 조회할 수 없는 경우 예외를 발생한다")
    void findShortestDistance_NotExistStationError() {
        Subway lines = new Subway(List.of(line2, line5));
        Station start = new Station("낙성대");
        Station end = new Station("동인천");
        DijkstraRouteStrategy dijkstraRouteStrategy = new DijkstraRouteStrategy();

        assertThatThrownBy(() -> dijkstraRouteStrategy.findShortestDistance(lines, start, end))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 역이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("노선의 역들이 연결되어있지 않아, 최단 거리를 조회할 수 없는 경우 예외를 발생한다")
    void findShortestDistance_NotConnectedError() {
        Subway lines = new Subway(List.of(line2, line3, line4, line5, line7));
        Station start = new Station("낙성대");
        Station end = new Station("천호");
        DijkstraRouteStrategy dijkstraRouteStrategy = new DijkstraRouteStrategy();

        assertThatThrownBy(() -> dijkstraRouteStrategy.findShortestDistance(lines, start, end))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이동할 수 없는 경로입니다.");
    }

    @Test
    @DisplayName("1번, 2번 환승해야 하는 경우 둘다 존재할 때, 최단 거리 경로를 찾아 반환한다")
    void findShortestDistance() {
            Subway lines = new Subway(List.of(line2, line3, line4, line7));
        Station start = new Station(1L,"낙성대");
        Station end = new Station(6l, "고속터미널");
        DijkstraRouteStrategy dijkstraRouteStrategy = new DijkstraRouteStrategy();

        Distance expected = new Distance(20);

        assertThat(dijkstraRouteStrategy.findShortestDistance(lines, start, end))
                .isEqualTo(expected);
    }
}
