package subway.application.costpolicy;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.Line;
import subway.domain.Path;
import subway.domain.StationInformation;

class LineCostPolicyChainTest {

    private static final long DEFAULT_COST = 1250L;
    private static final int AGE = 10;
    final CostPolicyChain costPolicyChain = new LineCostPolicyChain();

    @Test
    @DisplayName("호선에 추가요금이 존재할 때 요금을 계산한다.")
    void testCalculateLineCostExist() {
        //given
        final StationInformation stationInformation1 = new StationInformation(null, new Line("1호선", "검은색", 900L));
        final StationInformation stationInformation2 = new StationInformation(null, new Line("2호선", "초록색", 500L));
        final List<StationInformation> stationInformations = List.of(stationInformation1, stationInformation2);
        final Path path = new Path(stationInformations, null);

        //when
        final long result = costPolicyChain.calculate(path, AGE, DEFAULT_COST);

        //then
        assertThat(result).isEqualTo(DEFAULT_COST + stationInformation1.getLineCharge());
    }

    @Test
    @DisplayName("호선에 추가요금이 존재하지 않을 때 요금을 계산한다.")
    void testCalculateLineNotExist() {
        //given
        final StationInformation stationInformation1 = new StationInformation(null, new Line("1호선", "검은색", 0L));
        final StationInformation stationInformation2 = new StationInformation(null, new Line("2호선", "초록색", 0L));
        final List<StationInformation> stationInformations = List.of(stationInformation1, stationInformation2);
        final Path path = new Path(stationInformations, null);

        //when
        final long result = costPolicyChain.calculate(path, AGE, DEFAULT_COST);

        //then
        assertThat(result).isEqualTo(DEFAULT_COST);
    }
}
