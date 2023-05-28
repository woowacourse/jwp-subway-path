package subway.domain.fare;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.domain.subwaymap.Line;
import subway.domain.subwaymap.LineStation;
import subway.domain.subwaymap.Station;

@DisplayName("요금 계산 기능")
class FareCalculatorTest {

    @ParameterizedTest(name = "노선의 추가거리가 100원이고 거리가 {0}, 나이가 {1}일 때 운임요금은 {2}이다.")
    @CsvSource(value = {
        "10:19:1350",
        "10:18:1150",
        "10:12:850",
        "31:19:1850",
        "31:18:1550",
        "31:12:1100",
        "59:19:2350",
        "59:18:1950",
        "59:12:1350",
    }, delimiter = ':')
    void calculate(final int distance, final int age, final int expectFare) {
        //given
        final FarePolicies farePolicy = new FarePolicies();
        final List<LineStation> path = List.of(
            LineStation.of(Line.withNullId("line", "빨강", 100), Station.from("첫번째역")));

        //when
        final int budget = farePolicy.calculate(0, path, distance, age);

        //then
        Assertions.assertThat(budget).isEqualTo(expectFare);
    }

    @ParameterizedTest(name = "나이가 {0}일 때 오류를 던진다.")
    @CsvSource(value = {"-1", "0"})
    void calculate_fail_invalidAge(final int age) {
        //given
        final FarePolicies farePolicy = new FarePolicies();
        final List<LineStation> path = List.of(
            LineStation.of(Line.withNullId("line", "빨강", 100), Station.from("첫번째역")));

        //when
        //then
        Assertions.assertThatThrownBy(() -> farePolicy.calculate(0, path, 1, age))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest(name = "거리가 {0}일 때 오류를 던진다.")
    @CsvSource(value = {"-1"})
    void calculate_fail_invalidDistance(final int distance) {
        //given
        final FarePolicies farePolicy = new FarePolicies();
        final List<LineStation> path = List.of(
            LineStation.of(Line.withNullId("line", "빨강", 100), Station.from("첫번째역")));

        //when
        //then
        Assertions.assertThatThrownBy(() -> farePolicy.calculate(0, path, distance, 1))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
