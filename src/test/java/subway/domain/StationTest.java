package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Station은 ")
class StationTest {

    @ParameterizedTest
    @CsvSource(value = {"잠실,true", "선릉,false"})
    @DisplayName("역 이름이 동일한지 아닌지 확인할 수 있다.")
    void isSameNameTest(String name, boolean expected) {
        // given
        Station station = Station.of(1L, "잠실");

        // when
        boolean result = station.isSameName(Station.from(name));

        // then
        assertThat(result).isEqualTo(expected);
    }

}
