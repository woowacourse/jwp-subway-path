package subway.path.domain.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.line.exception.line.LineException;
import subway.path.domain.Path;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("DefaultPaymentPolicy 은(는)")
class DefaultPaymentPolicyTest {

    private final DefaultPaymentPolicy paymentPolicy = new DefaultPaymentPolicy();
    private final Path route = mock(Path.class);

    @Test
    void 거리가_없는경우_예외() {
        // given
        given(route.totalDistance()).willReturn(0);

        // when
        final String message = assertThrows(LineException.class, () ->
                paymentPolicy.calculateFee(route)
        ).getMessage();

        // then
        assertThat(message).contains("경로의 거리는 0일 수 없습니다");
    }

    @ParameterizedTest(name = "기본운임(10km 이내)인 경우 1250원이다. - {0}km")
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void 기본운임인_경우_1250_원이다(final int distance) {
        // given
        given(route.totalDistance()).willReturn(distance);

        // when
        final int payment = paymentPolicy.calculateFee(route);

        // then
        assertThat(payment).isEqualTo(1250);
    }

    @ParameterizedTest(name = "기본운임 거리 초과시 50km를 초과하지 않는 경우 5km 마다 100원 추가- {0}km일 때 {1} 원")
    @CsvSource(value = {
            "11,1350",
            "15,1350",

            "16,1450",
            "20,1450",

            "21,1550",
            "25,1550",
            "41,1950",
            "45,1950",
            "46,2050",
            "50,2050"

    })
    void 기본운임_거리_초과시_50km를_초과하지_않은_경우_5km_마다_100원_추가(final int distance, final int expected) {
        // given
        given(route.totalDistance()).willReturn(distance);

        // when
        final int payment = paymentPolicy.calculateFee(route);

        // then
        assertThat(payment).isEqualTo(expected);
    }

    @ParameterizedTest(name = "50km를 초과부터는 8km 마다 100원 추가- {0}km일 때 {1} 원")
    @CsvSource(value = {
            "51,2150",
            "58,2150",

            "59,2250",
            "66,2250",
            "67,2350",

    })
    void 기본운임_거리_초과시_50km_이후부터는_8km_마다_100원_추가(final int distance, final int expected) {
        // given
        given(route.totalDistance()).willReturn(distance);

        // when
        final int payment = paymentPolicy.calculateFee(route);

        // then
        assertThat(payment).isEqualTo(expected);
    }
}
