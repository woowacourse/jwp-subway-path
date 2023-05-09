package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LineTest {

    @CsvSource({"A, true", "D, false"})
    @ParameterizedTest(name = "노선 연결 상태가 [A-B-C]일 때 {0}을 포함하는지 확인한다. 결과: {1}")
    void 하나의_역이_존재하는지_확인한다(final String name, final boolean result) {
        // given
        List<Section> sections = List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 5)
        );
        final Line line = new Line("2호선", "RED", sections);

        // expect
        assertThat(line.contains(new Station(name))).isEqualTo(result);
    }

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
}
