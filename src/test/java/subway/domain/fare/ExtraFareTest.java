package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.fixture.LineFixture.추가요금_있는_이호선_팔호선_구간을_포함한_응답들;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExtraFareTest {

    @Test
    @DisplayName("주어진 노선 중 가장 큰 추가 요금을 반환한다.")
    void fare() {
        // given
        final ExtraFare extraFare = new ExtraFare(추가요금_있는_이호선_팔호선_구간을_포함한_응답들());

        // expected
        assertThat(extraFare.fare().fare())
            .isEqualTo(500);
    }
}
