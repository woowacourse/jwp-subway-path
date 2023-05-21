package subway.domain.station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class StationTest {

    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("역의 이름이 비어있으면 예외가 발생해야 한다.")
    void create_blank(String name) {
        // expect
        assertThatThrownBy(() -> new Station(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("역의 이름은 빈 값이 될 수 없습니다.");
    }

    @Test
    @DisplayName("역의 이름이 10글자를 초과하면 예외가 발생해야 한다.")
    void create_overThan10Characters() {
        // expect
        assertThatThrownBy(() -> new Station("1234567890역"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("역의 이름은 10글자를 초과할 수 없습니다.");
    }

    @Test
    @DisplayName("역의 이름이 역으로 끝나지 않으면 예외가 발생해야 한다.")
    void create_notEnd_역() {
        // expect
        assertThatThrownBy(() -> new Station("잠실"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("역의 이름은 \"역\"으로 끝나야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"잠실역", "12345678역"})
    @DisplayName("역이 정상적으로 생성되어야 한다.")
    void create_success(String name) {
        // when
        Station station = new Station(name);

        // then
        assertThat(station.getName())
                .isEqualTo(name);
    }
}
