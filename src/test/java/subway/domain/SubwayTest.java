package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.domain.Direction.LEFT;
import static subway.fixture.SubwayFixtures.SUBWAY1;
import static subway.fixture.SubwayFixtures.SUBWAY2;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.exception.InvalidSectionException;
import subway.exception.LineNotEmptyException;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SubwayTest {

    @Test
    void 역_추가시_전체_라인에서_등록할_구간이_존재하면_예외를_던진다() {
        // given
        final Line line1 = new Line("1호선", "RED", List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 5)
        ));
        final Line line2 = new Line("2호선", "RED", List.of(
                new Section("Z", "B", 5),
                new Section("B", "Y", 5)
        ));
        final Subway subway = new Subway(List.of(line1, line2));

        // expect
        assertThatThrownBy(() -> subway.add("1호선", "B", "Y", 5, LEFT))
                .isInstanceOf(InvalidSectionException.class)
                .hasMessage("지하철 전체 노선에 이미 존재하는 구간입니다.");
    }

    @Test
    void 역_추가시_입력한_노선_이름이_존재하지_않으면_예외를_던진다() {
        // given
        final Subway subway = new Subway(Collections.emptyList());

        // expect
        assertThatThrownBy(() -> subway.add("1호선", "B", "Y", 5, LEFT))
                .isInstanceOf(LineNotFoundException.class)
                .hasMessage("노선을 찾을 수 없습니다.");
    }

    @Test
    void 노선에_구간이_정상적으로_등록된다() {
        // given
        final Subway subway = SUBWAY1;

        // when
        subway.add("1호선", "B", "D", 3, LEFT);

        // then
        assertThat(subway.getLines()).flatExtracting(Line::getSections).containsAll(List.of(
                new Section("A", "D", 2),
                new Section("D", "B", 3),
                new Section("B", "C", 5),
                new Section("Z", "B", 5),
                new Section("B", "Y", 5)
        ));
    }

    @Test
    void 역_제거시_입력한_노선_이름이_존재하지_않으면_예외를_던진다() {
        // given
        final Subway subway = new Subway(Collections.emptyList());

        // expect
        assertThatThrownBy(() -> subway.remove("1호선", "B"))
                .isInstanceOf(LineNotFoundException.class)
                .hasMessage("노선을 찾을 수 없습니다.");
    }

    @Test
    void 노선에_구간이_정상적으로_제거된다() {
        // given
        final Subway subway = new Subway(List.of(
                new Line("1호선", "RED", List.of(
                        new Section("A", "B", 5),
                        new Section("B", "C", 5)
                ))
        ));

        // when
        subway.remove("1호선", "B");

        // then
        assertThat(subway.getLines()).flatExtracting(Line::getSections).containsAll(List.of(
                new Section("A", "C", 10)
        ));
    }

    @Test
    void 노선에_비어있을_때_초기_구간을_추가한다() {
        // given
        final Subway subway = new Subway(List.of(
                new Line("2호선", "RED", Collections.emptyList())
        ));

        // when
        subway.initialAdd("2호선", "A", "B", 4);

        // then
        assertThat(subway.getLines()).flatExtracting(Line::getSections).containsAll(List.of(
                new Section("A", "B", 4)
        ));
    }

    @Test
    void 초기_구간을_추가할_때_노선이_비어있지_않은_경우_예외를_던진다() {
        // given
        final Subway subway = new Subway(List.of(
                new Line("2호선", "RED", List.of(
                        new Section("B", "C", 3)
                ))
        ));

        // expect
        assertThatThrownBy(() -> subway.initialAdd("2호선", "A", "B", 4))
                .isInstanceOf(LineNotEmptyException.class)
                .hasMessage("노선이 비어있지 않습니다.");
    }

    @Test
    void 초기_구간을_추가할_때_이름이_같은_역을_추가하려는_경우_예외를_던진다() {
        // given
        final Subway subway = new Subway(List.of(
                new Line("2호선", "RED", Collections.emptyList())
        ));

        // expect
        assertThatThrownBy(() -> subway.initialAdd("2호선", "A", "A", 4))
                .isInstanceOf(InvalidSectionException.class)
                .hasMessage("동일한 이름을 가진 역을 구간에 추가할 수 없습니다.");
    }

    @Test
    void 입력받은_라인_이름으로_라인을_찾는다() {
        // given
        final Subway subway = SUBWAY1;

        // when
        Line line = subway.findLineByLineName("2호선");

        // then
        assertAll(
                () -> assertThat(line.getName()).isEqualTo("2호선"),
                () -> assertThat(line.getColor()).isEqualTo("BLUE"),
                () -> assertThat(line.getSections()).containsAll(List.of(
                        new Section("Z", "B", 5),
                        new Section("B", "Y", 5)
                ))
        );
    }

    @Test
    void 입력받은_출발역과_도착역의_최단경로를_조회한다_상행에서_하행방향의_경우() {
        // given
        final Subway subway = SUBWAY2;

        // when
        final Path shortestPath = subway.findShortestPath("A", "D");

        // then
        assertAll(
                () -> assertThat(shortestPath.getPath()).containsExactly(
                        new Station("A"), new Station("B"), new Station("D")),
                () -> assertThat(shortestPath.getDistance()).isEqualTo(new Distance(4))
        );
    }

    @Test
    void 입력받은_출발역과_도착역의_최단경로를_조회한다_하행에서_상행방향의_경우() {
        // given
        final Subway subway = SUBWAY2;

        // when
        final Path shortestPath = subway.findShortestPath("D", "A");

        // then
        assertAll(
                () -> assertThat(shortestPath.getPath()).containsExactly(
                        new Station("D"), new Station("B"), new Station("A")),
                () -> assertThat(shortestPath.getDistance()).isEqualTo(new Distance(4))
        );
    }

    @Test
    void 입력받은_출발역이_존재하지_않으면_예외를_던진다() {
        // given
        final Subway subway = SUBWAY2;

        // expect
        assertThatThrownBy(() -> subway.findShortestPath("Q", "B"))
                .isInstanceOf(StationNotFoundException.class)
                .hasMessageContaining("역을 찾을 수 없습니다.");
    }

    @Test
    void 입력받은_도착역이_존재하지_않으면_예외를_던진다() {
        // given
        final Subway subway = SUBWAY2;

        // expect
        assertThatThrownBy(() -> subway.findShortestPath("B", "Q"))
                .isInstanceOf(StationNotFoundException.class)
                .hasMessageContaining("역을 찾을 수 없습니다.");
    }
}
