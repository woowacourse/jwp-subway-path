package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.routestrategy.DijkstraRouteStrategy;
import subway.domain.routestrategy.RouteStrategy;

class SubwayTest {

    private static final Sections sectionsOfLine2 = new Sections(List.of(
            new Section(new Station(1L,"낙성대"), new Station(2L,"사당"), new Distance(5)),
            new Section(new Station(2L,"사당"), new Station(3L,"교대"), new Distance(5)),
            new Section(new Station(3L,"교대"), new Station(4L,"방배"), new Distance(5)))
    );
    private static final Sections sectionsOfLine3 = new Sections(List.of(
            new Section(new Station(5L,"잠원"), new Station(6L,"고속터미널"), new Distance(5)),
            new Section(new Station(6L,"고속터미널"), new Station(3L,"교대"), new Distance(500)),
            new Section(new Station(3L,"교대"), new Station(8L,"남부터미널"), new Distance(5)))
    );
    private static final Sections sectionsOfLine4 = new Sections(List.of(
            new Section(new Station(2L,"사당"), new Station(10L,"이수"), new Distance(5)),
            new Section(new Station(10L, "이수"), new Station(11L,"동작"), new Distance(5)))
    );
    private static final Sections sectionsOfLine5 = new Sections(List.of(
            new Section(new Station(12L,"천호"), new Station(13L,"강동"), new Distance(5)))
    );
    private static final Sections sectionsOfLine7 = new Sections(List.of(
            new Section(new Station(14L,"이수"), new Station(15L,"내방"), new Distance(5)),
            new Section(new Station(15L,"내방"), new Station(6L,"고속터미널"), new Distance(5)),
            new Section(new Station(6L,"고속터미널"), new Station(16L,"반포"), new Distance(5)))
    );

    private static final Line line1 = new Line(1L, "1호선", "파랑", new Sections());
    private static final Line line2 = new Line(2L, "2호선", "초록", sectionsOfLine2);
    private static final Line line3 = new Line(3L, "3호선", "주황", sectionsOfLine3);
    private static final Line line4 = new Line(4L, "4호선", "하늘", sectionsOfLine4);
    private static final Line line5 = new Line(5L, "5호선", "보라", sectionsOfLine5);
    private static final Line line7 = new Line(7L, "7호선", "올리브", sectionsOfLine7);

    @Test
    @DisplayName("노선들의 이름과 색상이 중복되지 않으면 정상 생성된다")
    void createTest() {
        List<Line> lines = List.of(
                new Line("1호선", "파랑", new Sections()),
                new Line("2호선", "초록", new Sections())
        );

        assertDoesNotThrow(() -> new Subway(lines));
    }

    @Test
    @DisplayName("새롭게 추가되는 노선의 이름이 기존 노선들과 중복 되면 에러가 발생한다")
    void addLine_NameDuplicateError() {
        Subway subway = new Subway(List.of(
                new Line("1호선", "파랑", new Sections()),
                new Line("2호선", "초록", new Sections())
        ));

        Line lineDuplicateName = new Line("1호선", "검정", new Sections());

        assertThatThrownBy(() -> subway.addLine(lineDuplicateName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("노선의 이름은 중복될 수 없습니다.");
    }


    @Test
    @DisplayName("새롭게 추가되는 노선의 색상이 기존 노선들과 중복 되면 에러가 발생한다")
    void addLine_ColorDuplicateError() {
        Subway subway = new Subway(List.of(
                new Line("1호선", "파랑", new Sections()),
                new Line("2호선", "초록", new Sections())
        ));

        Line lineDuplicateColor = new Line("3호선", "파랑", new Sections());

        assertThatThrownBy(() -> subway.addLine(lineDuplicateColor))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("노선의 색상은 중복될 수 없습니다.");
    }

    @Test
    @DisplayName("새롭게 추가되는 노선의 이름과 색상이 기존 노선들과 중복되지 않으면 정상 생성된다")
    void addLine() {
        Subway subway = new Subway(List.of(
                new Line("1호선", "파랑", new Sections()),
                new Line("2호선", "초록", new Sections())
        ));

        Line newLine = new Line("3호선", "주황", new Sections());
        assertDoesNotThrow(() -> subway.addLine(newLine));
    }

    @Test
    @DisplayName("기존에 존재하지 않는 노선을 삭제하려고 하면 에러가 발생한다")
    void removeLine_NotExistError() {
        Subway subway = new Subway(List.of(
                new Line(1L, "1호선", "파랑", new Sections()),
                new Line(2L, "2호선", "초록", new Sections())
        ));

        Line lineToRemove = new Line("3호선", "주황", new Sections());

        assertThatThrownBy(() -> subway.removeLine(lineToRemove))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 역이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("기존에 존재하는 노선을 삭제하려고 하면 정상적으로 삭제된다")
    void deleteLine() {
        Subway subway = new Subway(List.of(
                new Line(1L, "1호선", "파랑", new Sections()),
                new Line(2L, "2호선", "초록", new Sections())
        ));

        Line lineToRemove = new Line(1L,"1호선", "파랑", new Sections());

        assertDoesNotThrow(() -> subway.removeLine(lineToRemove));
    }

    @Test
    @DisplayName("수정하려는 노선의 이름이 기존 노선들과 중복 되면 에러가 발생한다")
    void updateLineName_DuplicateError() {
        Subway subway = new Subway(List.of(
                new Line(1L, "1호선", "파랑", new Sections()),
                new Line(2L, "2호선", "초록", new Sections())
        ));

        assertThatThrownBy(() -> subway.updateLineName(1L, "2호선"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("노선의 이름은 중복될 수 없습니다.");
    }

    @Test
    @DisplayName("기존에 존재하지 않는 노선을 수정하려고 하면 에러가 발생한다")
    void updateLineName_NotExistError() {
        Subway subway = new Subway(List.of(
                new Line(1L, "1호선", "파랑", new Sections()),
                new Line(2L, "2호선", "초록", new Sections())
        ));
        Long notExistLineId = 11111L;

        assertThatThrownBy(() -> subway.updateLineName(notExistLineId, "3호선"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 노선이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("기존의 노선과 중복되지 않는 이름으로 수정하면 정상적으로 수정된다")
    void updateLineName() {
        Subway subway = new Subway(List.of(
                new Line(1L, "1호선", "파랑", new Sections()),
                new Line(2L, "2호선", "초록", new Sections())
        ));
        Long lineId = 1L;

        assertDoesNotThrow(() -> subway.updateLineName(lineId, "3호선"));
        assertThat(subway.findLineById(lineId).getName()).isEqualTo("3호선");
    }

    @Test
    @DisplayName("수정하려는 노선의 색상이 기존 노선들과 중복 되면 에러가 발생한다")
    void updateLineColor_DuplicateError() {
        Subway subway = new Subway(List.of(
                new Line(1L, "1호선", "파랑", new Sections()),
                new Line(2L, "2호선", "초록", new Sections())
        ));

        assertThatThrownBy(() -> subway.updateLineColor(1L, "초록"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("노선의 색상은 중복될 수 없습니다.");
    }

    @Test
    @DisplayName("기존에 존재하지 않는 노선을 수정하려고 하면 에러가 발생한다")
    void updateLineColor_NotExistError() {
        Subway subway = new Subway(List.of(
                new Line(1L, "1호선", "파랑", new Sections()),
                new Line(2L, "2호선", "초록", new Sections())
        ));
        Long notExistLineId = 11111L;

        assertThatThrownBy(() -> subway.updateLineColor(notExistLineId, "노랑"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 노선이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("기존의 노선과 중복되지 않는 색상으로 수정하면 정상적으로 수정된다")
    void updateLineColor() {
        Subway subway = new Subway(List.of(
                new Line(1L, "1호선", "파랑", new Sections()),
                new Line(2L, "2호선", "초록", new Sections())
        ));
        Long lineId = 1L;

        assertDoesNotThrow(() -> subway.updateLineColor(lineId, "검정"));
        assertThat(subway.findLineById(lineId).getColor()).isEqualTo("검정");
    }

    @Test
    @DisplayName("지하철에 조회하려는 노선의 id가 없으면 에러가 발생한다")
    void findLineById_NotExistError() {
        Subway subway = new Subway(List.of(
                new Line(1L, "1호선", "파랑", new Sections()),
                new Line(2L, "2호선", "초록", new Sections())
        ));
        Long notExistLineId = 11111L;

        assertThatThrownBy(() -> subway.findLineById(notExistLineId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 노선이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("지하철에 조회하려는 노선의 id가 있으면 정상 조회된다")
    void findLineById() {
        Subway subway = new Subway(List.of(
                new Line(1L, "1호선", "파랑", new Sections()),
                new Line(2L, "2호선", "초록", new Sections())
        ));

        assertThat(subway.findLineById(1L)).isEqualTo(new Line(1L, "1호선", "파랑", new Sections()));
    }

    @Test
    @DisplayName("최단 경로를 조회할 때, 존재하지 않는 역이면 예외를 발생한다")
    void findShortestRoute_NotExistStation() {
        Subway subway = new Subway(List.of(line1, line2, line3, line4, line5, line7));

        Station start = new Station("동인천");
        Station end = new Station("교대");
        RouteStrategy routeStrategy = new DijkstraRouteStrategy();

        assertThatThrownBy(() -> subway.findShortestDistance(start, end, routeStrategy))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 역이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("최단 경로를 조회할 때, 출발지와 도착지가 같은 역이면 예외를 발생한다")
    void findShortestRoute_SameStation() {
        Subway subway = new Subway(List.of(line1, line2, line3, line4, line5, line7));

        Station start = new Station("동인천");
        Station end = new Station("동인천");
        RouteStrategy routeStrategy = new DijkstraRouteStrategy();

        assertThatThrownBy(() -> subway.findShortestRoute(start, end, routeStrategy))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발지와 도착지가 같은 역입니다.");
    }

    @Test
    @DisplayName("최단 경로를 조회할 때, 이동할 수 없는 경로인 경우 예외를 발생한다")
    void findShortestRoute_NotConnected() {
        Subway subway = new Subway(List.of(line1, line2, line3, line4, line5, line7));

        Station start = new Station(7L,"교대");
        Station end = new Station(12L,"천호");
        RouteStrategy routeStrategy = new DijkstraRouteStrategy();

        assertThatThrownBy(() -> subway.findShortestRoute(start, end, routeStrategy))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이동할 수 없는 경로입니다.");
    }

    @Test
    @DisplayName("두 역 사이의 최단 경로가 정상 조회된다1")
    void findShortestRoute() {
        Subway subway = new Subway(List.of(line2, line3));

        Station start = new Station(1L,"낙성대");
        Station end = new Station(6L,"고속터미널");
        RouteStrategy routeStrategy = new DijkstraRouteStrategy();

        List<Station> expected = List.of(
                new Station(1L, "낙성대"),
                new Station(2L, "사당"),
                new Station(3L, "교대"),
                new Station(6L, "고속터미널")
        );

        //todo : usingRecursiveComparison 다시 배우기
        assertThat(subway.findShortestRoute(start, end, routeStrategy))
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("두 역 사이의 최단 경로가 정상 조회된다2")
    void findShortestRoute2() {
        Subway subway = new Subway(List.of(line2, line3, line4, line5, line7));

        Station start = new Station(1L,"낙성대");
        Station end = new Station(6L,"고속터미널");
        RouteStrategy routeStrategy = new DijkstraRouteStrategy();

        List<Station> expected = List.of(
                new Station(1L, "낙성대"),
                new Station(2L, "사당"),
                new Station(14L, "이수"),
                new Station(15L, "내방"),
                new Station(6L, "고속터미널")
        );
        //todo : usingRecursiveComparison 다시 배우기

        assertThat(subway.findShortestRoute(start, end, routeStrategy))
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("두 역 사이의 최단 거리가 정상 조회된다")
    void findShortestDistance() {
        Subway subway = new Subway(List.of(line1, line2, line3, line4, line5, line7));

        Station start = new Station(1L,"낙성대");
        Station end = new Station(6L,"고속터미널");
        RouteStrategy routeStrategy = new DijkstraRouteStrategy();

        Distance expected = new Distance(20);

        //todo : usingRecursiveComparison 다시 배우기
        assertThat(subway.findShortestDistance(start, end, routeStrategy))
                .isEqualTo(expected);
    }
}
