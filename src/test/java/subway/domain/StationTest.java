package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.domain.line.Station;
import subway.exception.InvalidFormatException;

class StationTest {

    @DisplayName("역이 정상적으로 생성된다.")
    @Test
    void createStation() {
        // given
        String name = "잠실역";

        // when
        Station station = new Station(null, name);

        // then
        assertThat(station.getName()).isEqualTo(name);
    }

    @DisplayName("역 이름에 공백이 들어가는 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   ", "잠실 새내역"})
    void createStationFailWithBlankName(String name) {
        // when, then
        assertThatThrownBy(() -> new Station(null, name))
                .isInstanceOf(InvalidFormatException.class)
                .hasMessageContaining("역 이름에는 공백이 허용되지 않습니다.");
    }

    @DisplayName("역 이름의 2이상 10이하가 아닌 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"강", "우아한테코크스백엔드역"})
    void createStationFailWithWrongLength(String name) {
        // when, then
        assertThatThrownBy(() -> new Station(null, name))
                .isInstanceOf(InvalidFormatException.class)
                .hasMessageContaining("역 이름은 2~10자까지 가능합니다.");
    }

}
