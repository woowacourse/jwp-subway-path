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
}
