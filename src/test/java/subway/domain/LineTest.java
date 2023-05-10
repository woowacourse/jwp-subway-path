package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.domain.Direction.LEFT;
import static subway.domain.Direction.RIGHT;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.InvalidSectionException;

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
        final Line line = new Line("2호선", "RED", sections);

        // expect
        assertThat(line.containsAll(new Station(start), new Station(end))).isEqualTo(result);
    }

    @Test
    void 노선에_기준_역이_없으면_예외를_던진다() {
        // given
        final Line line = new Line("2호선", "RED", List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 5)
        ));

        // expect
        assertThatThrownBy(() -> line.add(new Station("D"), new Station("C"), new Distance(3), LEFT))
                .isInstanceOf(InvalidSectionException.class)
                .hasMessage("기준역이 존재하지 않습니다.");
    }

    @Test
    void 노선에_추가할_역이_있으면_예외를_던진다() {
        // given
        final Line line = new Line("2호선", "RED", List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 5)
        ));

        // expect
        assertThatThrownBy(() -> line.add(new Station("C"), new Station("A"), new Distance(3), LEFT))
                .isInstanceOf(InvalidSectionException.class)
                .hasMessage("추가할 역이 이미 존재합니다.");
    }

    @ValueSource(ints = {5, 6})
    @ParameterizedTest(name = "{displayName} 입력: {0}")
    void 하행방향으로_추가시_기준역이_구간의_시작에_존재할_경우_추가할_구간의_거리가_기존_구간의_거리보다_같거나_크면_예외를_던진다(final int value) {
        // given
        final Line line = new Line("2호선", "RED", List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 5)
        ));

        // expect
        assertThatThrownBy(() -> line.add(new Station("A"), new Station("D"), new Distance(value), RIGHT))
                .isInstanceOf(InvalidSectionException.class)
                .hasMessage("추가할 구간의 거리가 기존 구간의 거리보다 같거나 클 수 없습니다.");
    }

    @ValueSource(ints = {5, 6})
    @ParameterizedTest(name = "{displayName} 입력: {0}")
    void 상행방향으로_추가시_기준역이_구간의_끝에_존재할_경우_추가할_구간의_거리가_기존_구간의_거리보다_같거나_크면_예외를_던진다(final int value) {
        // given
        final Line line = new Line("2호선", "RED", List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 5)
        ));

        // expect
        assertThatThrownBy(() -> line.add(new Station("C"), new Station("D"), new Distance(value), LEFT))
                .isInstanceOf(InvalidSectionException.class)
                .hasMessage("추가할 구간의 거리가 기존 구간의 거리보다 같거나 클 수 없습니다.");
    }
}
