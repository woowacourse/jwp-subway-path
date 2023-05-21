package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class CostTest {

    @ParameterizedTest(name = "기본 운임(10km 이하) 요금은 1250원이다.")
    @ValueSource(ints = {3, 5, 10})
    void calculateDefaultCost(int distance) {
        Cost cost = new Cost(new Distance(distance));

        assertThat(cost.getCost()).isEqualTo(1250);
    }

    @DisplayName("중거리 운행(10km ~ 50km)은 5km당 100원의 추가요금이 붙는다.")
    @Test
    void calculateMiddleDistanceCost() {
        Cost cost = new Cost(new Distance(23));

        //기본 요금 1250 + 추가 요금 500
        assertThat(cost.getCost()).isEqualTo(1750);
    }

    @DisplayName("장거리 운행(50km 초과)은 8km당 100원의 추가요금이 붙는다.")
    @Test
    void calculateLongDistanceCost() {
        Cost cost = new Cost(new Distance(55));

        //기본 요금 1250 + 추가 요금 700
        assertThat(cost.getCost()).isEqualTo(1950);
    }
}
