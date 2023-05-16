package subway.domain.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StationTest {

    @CsvSource({"A, true", "B, false"})
    @ParameterizedTest(name = "역 A와 이름이 같은지 확인한다. 입력: {0}, 결과: {1}")
    void 이름을_입력받아_현재_역이랑_이름이_같은지_확인한다(final String name, final boolean result) {
        // given
        final Station station = new Station("A");

        // expect
        assertThat(station.isSameName(name)).isEqualTo(result);
    }

    @CsvSource({"A, true", "B, false"})
    @ParameterizedTest(name = "역 A와 이름이 같은지 확인한다. 입력: {0}, 결과: {1}")
    void 다른_역을_입력받아_현재_역이랑_이름이_같은지_확인한다(final String name, final boolean result) {
        // given
        final Station station = new Station("A");

        // expect
        assertThat(station.isSameName(new Station(name))).isEqualTo(result);
    }
}
