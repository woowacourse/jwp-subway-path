package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Station은 ")
class StationTest {

    @ParameterizedTest
    @CsvSource(value = {"잠실역,true", "선릉역,false"})
    @DisplayName("역 이름이 동일한지 아닌지 확인할 수 있다")
    void isSameNameTest(String name, boolean expected) {
        // given
        Station station = new Station(1L, "잠실역");

        // when
        boolean result = station.isSameName(name);

        // then
        assertThat(result).isEqualTo(expected);
    }

}
