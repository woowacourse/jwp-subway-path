package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SectionTest {

    @CsvSource({"A, true", "C, false"})
    @ParameterizedTest(name = "Section(A, B)일 때 {0}을 포함하는지 확인한다. 결과: {1}")
    void 하나의_역이_존재하는지_확인한다(final String name, final boolean result) {
        // given
        final Section section = new Section("A", "B", 5);

        // expect
        assertThat(section.contains(new Station(name))).isEqualTo(result);
    }

    @CsvSource({"A, B, true", "B, C, false", "B, A, true"})
    @ParameterizedTest(name = "Section(A, B)일 때 {0}과 {1}을 모두 포함하는지 확인한다. 결과: {2}")
    void 두개_역_모두_존재하는지_확인한다(final String start, final String end, final boolean result) {
        // given
        final Section section = new Section("A", "B", 5);

        // expect
        assertThat(section.containsAll(new Station(start), new Station(end))).isEqualTo(result);
    }

    @ParameterizedTest(name = "Section(A, B)의 거리가 3일 때 입력받은 값이 크거나 같은지 확인한다. 입력: {0}, 결과: {1}")
    @CsvSource({"2, false", "3, true", "4, true"})
    void 자신의_거리보다_크거나_같은지_확인한다(final int value, final boolean result) {
        // given
        final Section section = new Section("A", "B", 3);

        // expect
        assertThat(section.moreThanOrEqual(new Distance(value))).isEqualTo(result);
    }
}
