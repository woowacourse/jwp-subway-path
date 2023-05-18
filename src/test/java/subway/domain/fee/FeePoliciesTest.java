package subway.domain.fee;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.domain.Line;
import subway.domain.Lines;


@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("FeePolicies 은(는)")
class FeePoliciesTest {

    private FeePolicies feePolicies = new FeePolicies(List.of(new LineFeePolicy(), new DistanceFeePolicy()));

    @Test
    void 추가_요금이_없으면_기본_요금을_반환한다() {
        // given
        FeeInformation feeInformation = new FeeInformation(4,
                new Lines(
                        List.of(
                                new Line("1호선", null, 0),
                                new Line("2호선", null, 0)
                        )
                )
        );

        // when
        int fee = feePolicies.calculate(feeInformation);

        // then
        assertThat(fee).isEqualTo(1250);
    }

    @Test
    void 조건이_맞으면_추가_요금을_포함하여_돈을_계산한다() {
        // given
        FeeInformation feeInformation = new FeeInformation(11,
                new Lines(
                        List.of(
                                new Line("1호선", null, 300),
                                new Line("2호선", null, 500)
                        )
                )
        );

        // when
        int fee = feePolicies.calculate(feeInformation);

        // then
        assertThat(fee).isEqualTo(1850);
    }
}
