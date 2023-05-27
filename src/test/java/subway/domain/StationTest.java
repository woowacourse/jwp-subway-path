package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StationTest {

    @CsvSource({"A, true", "B, false"})
    @ParameterizedTest(name = "Station의 이름이 A일 때 입력한 이름과 같은지 확인한다. 입력: {0} 결과: {1}")
    void 역의_이름이_같은지_확인한다(final String name, final boolean result) {
        // given
        Station station = new Station("A");

        // expect
        assertThat(station.isSame(name)).isEqualTo(result);
    }
}
