package subway.business.domain.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class StationTest {

    @DisplayName("이름 비교")
    @Nested
    class haveSameNameWith {

        @DisplayName("역의 이름을 입력받으면 true를 반환한다.")
        @Test
        void shouldReturnTrueWhenInputStationHaveSameName() {
            Station station = Station.from("잠실역");

            assertThat(station.hasNameOf("잠실역")).isTrue();
        }

        @DisplayName("역의 이름이 아닌 값을 입력받으면 false를 반환한다.")
        @Test
        void shouldReturnFalseWhenInputStationHaveDifferentName() {
            Station station = Station.from("잠실역");

            assertThat(station.hasNameOf("몽촌토성역")).isFalse();
        }
    }


    @DisplayName("동등성")
    @Nested
    class equals {

        @DisplayName("ID가 같은 Station을 비교하면 true를 반환한다.")
        @Test
        void shouldReturnTrueWhenCompareStationsHaveSameId() {
            Station station1 = new Station(1L, "잠실역");
            Station station2 = new Station(1L, "잠실역");

            assertThat(station1.equals(station2)).isTrue();
        }

        @DisplayName("ID가 다른 Station을 비교하면 false를 반환한다.")
        @Test
        void shouldReturnFalseWhenCompareStationsHaveDifferentId() {
            Station station1 = new Station(1L, "잠실역");
            Station station2 = new Station(2L, "잠실역");

            assertThat(station1.equals(station2)).isFalse();
        }

        @DisplayName("ID가 null인 Station을 기준으로 비교하면 예외가 발생한다.")
        @Test
        void shouldThrowExceptionWhenCompareFromStationHaveNullId() {
            Station station1 = new Station(null, "잠실역");
            Station station2 = new Station(2L, "잠실역");

            assertThatThrownBy(() -> station1.equals(station2))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("ID가 존재하지 않는 Station을 기준으로 비교했습니다.");
        }

        @DisplayName("ID가 null인 Station을 기준으로 비교하면 예외가 발생한다.")
        @Test
        void shouldThrowExceptionWhenCompareToStationHaveNullId() {
            Station station1 = new Station(1L, "잠실역");
            Station station2 = new Station(null, "잠실역");

            assertThatThrownBy(() -> station1.equals(station2))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("ID가 존재하지 않는 Station을 인자로 넣어 비교했습니다.");
        }
    }
}
