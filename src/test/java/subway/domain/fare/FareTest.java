package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.exception.ErrorMessage;
import subway.exception.InvalidException;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("요금 테스트")
class FareTest {
    @Test
    void 요금이_음수면_예외를_반환한다() {
        // then
        assertThatThrownBy(() -> new Fare(-1))
                .isInstanceOf(InvalidException.class)
                .hasMessage(ErrorMessage.INVALID_NEGATIVE_FARE.getErrorMessage());
    }

    @Test
    void 요금을_입력받아_증가된_요금값을_리턴한다() {
        // given
        final int 기본_값 = 10;
        final int 추가할_값 = 20;
        final Fare 기존_요금 = new Fare(기본_값);

        // when
        final Fare 추가된_요금 = 기존_요금.add(new Fare(추가할_값));

        // then
        assertThat(추가된_요금.getValue()).isEqualTo(기본_값 + 추가할_값);
    }
}
