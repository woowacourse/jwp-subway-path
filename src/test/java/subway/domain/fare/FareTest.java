package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.common.exception.SubwayIllegalArgumentException;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class FareTest {

    @Test
    void 요금이_0원보다_작으면_예외() {
        // given
        int fare = -1;

        // when then
        assertThatThrownBy(() -> new Fare(fare))
                .isInstanceOf(SubwayIllegalArgumentException.class)
                .hasMessage("요금은 0원 이상이어야 합니다.");
    }

    @Test
    void 요금을_합산한다() {
        // given
        Fare fare1 = new Fare(1000);
        Fare fare2 = new Fare(2000);

        // when
        Fare totalFare = fare1.plus(fare2);

        // then
        assertThat(totalFare).isEqualTo(new Fare(3000));
    }
}
