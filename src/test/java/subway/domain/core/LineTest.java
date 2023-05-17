package subway.domain.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.domain.core.Direction.LEFT;
import static subway.domain.core.Direction.RIGHT;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.InvalidSectionException;
import subway.exception.LineNotEmptyException;
import subway.exception.StationNotFoundException;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LineTest {

    @CsvSource({"A, B, true", "C, D, false", "B, A, true"})
    @ParameterizedTest(name = "노선 연결 상태가 [A-B-C]일 때 {0}과 {1}가 연결되어 있는지 확인한다. 결과: {2}")
    void 입력_받은_두_역이_연결되어_있는지_확인한다(final String start, final String end, final boolean result) {
        // given
        List<Section> sections = List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 5)
        );
        final Line line = new Line("2호선", "RED", 0, sections);

        // expect
        assertThat(line.containsAll(new Station(start), new Station(end))).isEqualTo(result);
    }

    @Test
    void 노선에_기준_역이_없으면_예외를_던진다() {
        // given
        final Line line = new Line("2호선", "RED", 0, List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 5)
        ));

        // expect
        assertThatThrownBy(() -> line.add(new Station("D"), new Station("C"), new Distance(3), LEFT))
                .isInstanceOf(InvalidSectionException.class)
                .hasMessage("기준역이 존재하지 않습니다.");
    }

    @Test
    void 노선에_등록할_역이_있으면_예외를_던진다() {
        // given
        final Line line = new Line("2호선", "RED", 0, List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 5)
        ));

        // expect
        assertThatThrownBy(() -> line.add(new Station("C"), new Station("A"), new Distance(3), LEFT))
                .isInstanceOf(InvalidSectionException.class)
                .hasMessage("등록할 역이 이미 존재합니다.");
    }

    @ValueSource(ints = {5, 6})
    @ParameterizedTest(name = "{displayName} 입력: {0}")
    void 하행방향으로_등록시_기준역이_구간의_시작에_존재할_경우_등록할_구간의_거리가_기존_구간의_거리보다_같거나_크면_예외를_던진다(final int value) {
        // given
        final Line line = new Line("2호선", "RED", 0, List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 5)
        ));

        // expect
        assertThatThrownBy(() -> line.add(new Station("A"), new Station("D"), new Distance(value), RIGHT))
                .isInstanceOf(InvalidSectionException.class)
                .hasMessage("등록할 구간의 거리가 기존 구간의 거리보다 같거나 클 수 없습니다.");
    }

    @ValueSource(ints = {5, 6})
    @ParameterizedTest(name = "{displayName} 입력: {0}")
    void 상행방향으로_등록시_기준역이_구간의_끝에_존재할_경우_등록할_구간의_거리가_기존_구간의_거리보다_같거나_크면_예외를_던진다(final int value) {
        // given
        final Line line = new Line("2호선", "RED", 0, List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 5)
        ));

        // expect
        assertThatThrownBy(() -> line.add(new Station("C"), new Station("D"), new Distance(value), LEFT))
                .isInstanceOf(InvalidSectionException.class)
                .hasMessage("등록할 구간의 거리가 기존 구간의 거리보다 같거나 클 수 없습니다.");
    }

    @Test
    void 이미_존재하는_구간_사이에_오른쪽으로_역을_새로_등록하는_경우_거리가_재계산된다() {
        // given
        final Line line = new Line("2호선", "RED", 0, List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 5)
        ));

        // when
        line.add(new Station("A"), new Station("D"), new Distance(3), RIGHT);

        // then
        assertThat(line.getSections()).containsAll(List.of(
                new Section("B", "C", 5),
                new Section("A", "D", 3),
                new Section("D", "B", 2)
        ));
    }

    @Test
    void 이미_존재하는_구간_사이에_왼쪽으로_역을_새로_등록하는_경우_거리가_재계산된다() {
        // given
        final Line line = new Line("2호선", "RED", 0, List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 5)
        ));

        // when
        line.add(new Station("C"), new Station("D"), new Distance(3), LEFT);

        // then
        assertThat(line.getSections()).containsAll(List.of(
                new Section("A", "B", 5),
                new Section("B", "D", 2),
                new Section("D", "C", 3)
        ));
    }

    @Test
    void 노선_오른쪽_끝에_구간을_등록한다() {
        // given
        final Line line = new Line("2호선", "RED", 0, List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 5)
        ));

        // when
        line.add(new Station("C"), new Station("D"), new Distance(3), RIGHT);

        // then
        assertThat(line.getSections()).containsAll(List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 5),
                new Section("C", "D", 3)
        ));
    }

    @Test
    void 노선_왼쪽_끝에_구간을_등록한다() {
        // given
        final Line line = new Line("2호선", "RED", 0, List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 5)
        ));

        // when
        line.add(new Station("A"), new Station("D"), new Distance(3), LEFT);

        // then
        assertThat(line.getSections()).containsAll(List.of(
                new Section("D", "A", 3),
                new Section("A", "B", 5),
                new Section("B", "C", 5)
        ));
    }

    @ParameterizedTest(name = "입력받은 노선명과 동일한지 확인한다. 기존: 1호선, 입력: {0}, 결과: {1}")
    @CsvSource({"1호선, true", "2호선, false"})
    void 입력받은_노선명과_동일한지_확인한다(final String name, final boolean result) {
        // given
        final Line line = new Line("1호선", "RED", 0, Collections.emptyList());

        // expect
        assertThat(line.isSameName(name)).isEqualTo(result);
    }

    @Test
    void 제거하려는_역이_없는_경우_예외를_던진다() {
        // given
        final Line line = new Line("2호선", "RED", 0, List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 5)
        ));

        // expect
        assertThatThrownBy(() -> line.remove(new Station("D")))
                .isInstanceOf(StationNotFoundException.class)
                .hasMessage("역을 찾을 수 없습니다.");
    }

    @Test
    void 중간에_있는_역을_제거한다() {
        // given
        final Line line = new Line("2호선", "RED", 0, List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 5)
        ));

        // when
        line.remove(new Station("B"));

        // then
        assertThat(line.getSections()).containsAll(List.of(
                new Section("A", "C", 10)
        ));
    }

    @Test
    void 상행_종점역을_제거한다() {
        // given
        final Line line = new Line("2호선", "RED", 0, List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 5)
        ));

        // when
        line.remove(new Station("A"));

        // then
        assertThat(line.getSections()).containsAll(List.of(
                new Section("B", "C", 5)
        ));
    }

    @Test
    void 하행_종점역을_제거한다() {
        // given
        final Line line = new Line("2호선", "RED", 0, List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 5)
        ));

        // when
        line.remove(new Station("C"));

        // then
        assertThat(line.getSections()).containsAll(List.of(
                new Section("A", "B", 5)
        ));
    }

    @Test
    void 남아있는_역이_두개인_경우_두_역_모두_제거한다() {
        // given
        final Line line = new Line("2호선", "RED", 0, List.of(
                new Section("A", "B", 5)
        ));

        // when
        line.remove(new Station("A"));

        // then
        assertThat(line.getSections()).isEmpty();
    }

    @Test
    void 노선에_포함된_역을_순서대로_보여줘야_한다() {
        // given
        final Line line = new Line("2호선", "RED", 0, List.of(
                new Section("B", "C", 3),
                new Section("A", "B", 2),
                new Section("D", "E", 5),
                new Section("C", "D", 4)
        ));

        // when
        List<Station> stations = line.findAllStation();

        // then
        assertThat(stations).containsExactly(
                new Station("A"),
                new Station("B"),
                new Station("C"),
                new Station("D"),
                new Station("E")
        );
    }

    @Test
    void 노선에_비어있는_경우_빈_리스트를_반환한다() {
        // given
        final Line line = new Line("2호선", "RED", 0, Collections.emptyList());

        // when
        List<Station> stations = line.findAllStation();

        // then
        assertThat(stations).isEmpty();
    }

    @Test
    void 노선에_비어있을_때_초기_구간을_추가한다() {
        // given
        final Line line = new Line("2호선", "RED", 0, Collections.emptyList());

        // when
        line.initialAdd(new Section("A", "B", 3));

        // then
        assertThat(line.getSections()).contains(new Section("A", "B", 3));
    }

    @Test
    void 초기_구간을_추가할_때_노선이_비어있지_않은_경우_예외를_던진다() {
        // given
        final Line line = new Line("2호선", "RED", 0, List.of(
                new Section("B", "C", 3)
        ));

        // expect
        assertThatThrownBy(() -> line.initialAdd(new Section("A", "B", 3)))
                .isInstanceOf(LineNotEmptyException.class)
                .hasMessage("노선이 비어있지 않습니다.");
    }

    @Test
    void 이름과_색을_변경한다() {
        // given
        final Line line = new Line("2호선", "RED", 0, Collections.emptyList());

        // when
        line.changeNameAndColor("3호선", "ORANGE");

        // then
        assertAll(
                () -> assertThat(line.getName()).isEqualTo("3호선"),
                () -> assertThat(line.getColor()).isEqualTo("ORANGE")
        );
    }
}

