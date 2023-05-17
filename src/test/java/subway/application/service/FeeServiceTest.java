package subway.application.service;

import org.junit.jupiter.api.Test;
import subway.dto.response.ShortestWayResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class FeeServiceTest extends ServiceTest {

    @Test
    void 가장_짧은_경로와_요금을_계산한다() {
        final ShortestWayResponse shortestWayResponse = feeService.showShortestWay(1L, 2L);

        assertAll(
                () -> assertThat(shortestWayResponse.getFee()).isEqualTo(1250),
                () -> assertThat(shortestWayResponse.getStations()).hasSize(2)
        );
    }
}
