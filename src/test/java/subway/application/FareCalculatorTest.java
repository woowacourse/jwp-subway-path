package subway.application;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.fare.Fare;
import subway.domain.fare.TotalDistance;


@ExtendWith(MockitoExtension.class)
class FareCalculatorTest {

    @InjectMocks
    private FareCalculator fareCalculator;

    @ParameterizedTest(name = "기본 요금을 반환한다.")
    @ValueSource(ints = {9, 10})
    void calculateFare_10km_lower(final int distance) {
        // given
        final TotalDistance totalDistance = new TotalDistance(distance);

        // when
        final Fare fare = fareCalculator.calculateFare(totalDistance);

        // then
        assertThat(fare.fare())
            .isEqualTo(1250);
    }

    @ParameterizedTest(name = "10~50km 범위의 거리라면, 기본 거리를 제외한 나머지 거리에 대해 5km마다 100원씩 부과하여 요금을 계산한다.")
    @CsvSource(value = {"12:1350", "16:1450"}, delimiter = ':')
    void calculateFare_10km_50km(final int distance, final int fare) {
        // given
        final TotalDistance totalDistance = new TotalDistance(distance);

        // when
        final Fare result = fareCalculator.calculateFare(totalDistance);

        // then
        assertThat(result.fare())
            .isEqualTo(fare);
    }

    @ParameterizedTest(name = "50km 초과의 범위의 거리라면, 50km까지는 5km당 100원, 그 이후는 8km당 100원씩 부과하여 요금을 계산한다.")
    @CsvSource(value = {"58:2150", "59:2250"}, delimiter = ':')
    void calculateFare_50km_upper(final int distance, final int fare) {
        // given
        final TotalDistance totalDistance = new TotalDistance(distance);

        // when
        final Fare result = fareCalculator.calculateFare(totalDistance);

        // then
        assertThat(result.fare())
            .isEqualTo(fare);
    }

    @Test
    @DisplayName("청소년 요금 할인 정책을 적용한 요금을 반환한다.")
    void calculateTeenagerFare() {
        // given
        final Fare fare = new Fare(1000);

        // when
        final Fare teenagerFare = fareCalculator.calculateTeenagerFare(fare);

        // then
        assertThat(teenagerFare.fare())
            .isEqualTo(520);
    }

    @Test
    @DisplayName("어린이 요금 할인 정책을 적용한 요금을 반환한다.")
    void calculateChildFare() {
        // given
        final Fare fare = new Fare(1000);

        // when
        final Fare teenagerFare = fareCalculator.calculateChildFare(fare);

        // then
        assertThat(teenagerFare.fare())
            .isEqualTo(325);
    }
}
