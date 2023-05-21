package subway.domain.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
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

    @CsvSource({"A, LEFT, true", "B, LEFT, false", "B, RIGHT, true", "B, LEFT, false"})
    @ParameterizedTest(name = "Section(A, B)일 때 {0}이 {1} 위치에 존재하는지 확인한다. 결과: {2}")
    void 입력받은_역이_입력받은_위치에_존재하는지_확인한다(final String name, final Direction direction, final boolean result) {
        // given
        final Section section = new Section("A", "B", 5);

        // expect
        assertThat(section.isStationExistsAtDirection(new Station(name), direction)).isEqualTo(result);
    }

    @Test
    void 현재_거리에서_입력받은_거리를_뺀_값을_반환한다() {
        // given
        final Section section = new Section("A", "B", 5);

        // when
        final Distance result = section.subtract(new Distance(2));

        // then
        assertThat(result).isEqualTo(new Distance(3));
    }

    @Test
    void 현재_거리에서_입력받은_거리를_더한_값을_반환한다() {
        // given
        final Section section = new Section("A", "B", 5);

        // when
        final Distance result = section.add(new Distance(2));

        // then
        assertThat(result).isEqualTo(new Distance(7));
    }

    @Test
    void 시작역_이름을_반환한다() {
        // given
        final Section section = new Section("A", "B", 5);

        // when
        final String result = section.getStartName();

        // then
        assertThat(result).isEqualTo("A");
    }

    @Test
    void 도착역_이름을_반환한다() {
        // given
        final Section section = new Section("A", "B", 5);

        // when
        final String result = section.getEndName();

        // then
        assertThat(result).isEqualTo("B");
    }

    @Test
    void 거리값을_반환한다() {
        // given
        final Section section = new Section("A", "B", 5);

        // when
        final Integer result = section.getDistanceValue();

        // then
        assertThat(result).isEqualTo(5);
    }
}
