package subway.domain.fee;

import org.junit.jupiter.api.Test;
import subway.domain.subway.Distance;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NormalFeeStrategyTest {

    @Test
    void 기본_요금_계산() {
        NormalFeeStrategy feeStrategy = new NormalFeeStrategy();
        Distance distance = new Distance(5); // 예시 거리: 5
        int fee = feeStrategy.calculateFee(distance);
        assertEquals(1250, fee);
    }

    @Test
    void 요금_계산_10_50() {
        NormalFeeStrategy feeStrategy = new NormalFeeStrategy();
        Distance distance = new Distance(16); // 예시 거리: 30
        int fee = feeStrategy.calculateFee(distance);
        assertEquals(1450, fee);
    }

    @Test
    void 요금_계산_50_이상() {
        NormalFeeStrategy feeStrategy = new NormalFeeStrategy();
        Distance distance = new Distance(58); // 예시 거리: 70
        int fee = feeStrategy.calculateFee(distance);
        assertEquals(2150, fee);
    }
}
