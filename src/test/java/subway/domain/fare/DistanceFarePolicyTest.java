package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class DistanceFarePolicyTest {

    DistanceFarePolicy distanceFarePolicy;

    @BeforeEach
    void setUp() {
        distanceFarePolicy = new DistanceFarePolicy();
    }

    @Test
    void calculate_메소드는_거리가_9km_인_경우_기본_요금_1250을_반환한다() {
        final FareAmount actual = distanceFarePolicy.calculate(9);

        assertThat(actual.getAmount()).isEqualTo(1250);
    }

    @Test
    void calculate_메소드는_거리가_12km_인_경우_추가_요금_1350을_반환한다() {
        final FareAmount actual = distanceFarePolicy.calculate(12);

        assertThat(actual.getAmount()).isEqualTo(1350);
    }

    @Test
    void calculate_메소드는_거리가_12km_인_경우_추가_요금_1450을_반환한다() {
        final FareAmount actual = distanceFarePolicy.calculate(16);

        assertThat(actual.getAmount()).isEqualTo(1450);
    }

    @Test
    void calculate_메소드는_거리가_58km_인_경우_추가_요금_2150을_반환한다() {
        final FareAmount actual = distanceFarePolicy.calculate(58);

        assertThat(actual.getAmount()).isEqualTo(2150);
    }
}
