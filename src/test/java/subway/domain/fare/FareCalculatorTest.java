package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.domain.Line;
import subway.domain.Lines;
import subway.domain.fare.policy.AgeDiscountPolicy;
import subway.domain.fare.policy.DistanceFarePolicy;
import subway.domain.fare.policy.FarePolicies;
import subway.domain.fare.policy.LineFarePolicy;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("FareCalculator 은(는)")
class FareCalculatorTest {

    private final FareCalculator farePolicies = new FareCalculator(
            new FarePolicies(List.of(new LineFarePolicy(), new DistanceFarePolicy())),
            new AgeDiscountPolicy());

    @Test
    void 추가_요금이_없으면_기본_요금을_반환한다() {
        // given
        final Lines lines = new Lines(
                List.of(
                        new Line("1호선", null, 0),
                        new Line("2호선", null, 0)
                )
        );
        final FareInformation fareInformation = new FareInformation(4, lines, AgeGroup.ADULT);

        // when
        final int actual = farePolicies.calculate(fareInformation);

        // then
        assertThat(actual).isEqualTo(1250);
    }

    @Test
    void 조건이_맞으면_추가_요금을_포함하여_돈을_계산한다() {
        // given
        final Lines lines = new Lines(
                List.of(
                        new Line("1호선", null, 300),
                        new Line("2호선", null, 500)
                )
        );
        final FareInformation fareInformation = new FareInformation(11, lines, AgeGroup.CHILD);

        // when
        final int actual = farePolicies.calculate(fareInformation);

        // then
        assertThat(actual).isEqualTo(750);
    }
}
