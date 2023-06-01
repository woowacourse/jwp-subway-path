package subway.application.service;

import org.junit.jupiter.api.Test;
import subway.dto.response.ShortestPathResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class FeeServiceTest extends ServiceTest {

    @Test
    void 가장_짧은_경로와_요금을_계산한다() {
        final ShortestPathResponse shortestPathResponse = feeService.showShortestPath(1L, 2L);

        assertAll(
                () -> assertThat(shortestPathResponse.getFee()).isEqualTo(1250),
                () -> assertThat(shortestPathResponse.getStations()).hasSize(2)
        );
    }
}
