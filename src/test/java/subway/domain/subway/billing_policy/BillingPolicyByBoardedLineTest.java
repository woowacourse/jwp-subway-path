package subway.domain.subway.billing_policy;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.Path;
import subway.domain.line.Line;

class BillingPolicyByBoardedLineTest {

    @Test
    @DisplayName("이용한 노선 정보를 통해 적절한 요금 정보를 반환한다.")
    void calculateFare() {
        //given
        final Line lineTwo = new Line("2호선", "초록색", 700);
        final Line lineFour = new Line("4호선", "하늘색", 400);
        final Line lineNine = new Line("9호선", "노란색", 700);
        final Path path = new Path(Collections.emptyList(), List.of(lineTwo, lineFour, lineNine), Integer.MAX_VALUE);
        final BillingPolicyByBoardedLine billingPolicyByBoardedLine = new BillingPolicyByBoardedLine();

        //when
        final Fare fare = billingPolicyByBoardedLine.calculateFare(path);

        //then
        assertThat(fare.getValue()).isEqualTo(700);
    }

}
