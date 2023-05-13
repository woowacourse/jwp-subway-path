package subway.application.v2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.config.ServiceTestConfig;
import subway.dto.StationResponse;
import subway.ui.v2.CreateStationRequest;

import static org.assertj.core.api.Assertions.assertThat;

class StationServiceV2Test extends ServiceTestConfig {

    StationServiceV2 stationService;

    @BeforeEach
    void setUp() {
        stationService = new StationServiceV2(stationDaoV2);
    }

    @DisplayName("역을 등록한다.")
    @Test
    void saveStation() {
        // given
        final CreateStationRequest request = new CreateStationRequest("잠실");

        // when
        final Long saveStationId = stationService.saveStation(request);

        // then
        assertThat(saveStationId)
                .isNotNull()
                .isNotZero();
    }

    @DisplayName("역을 조회한다.")
    @Test
    void findByStationId() {
        // given
        final CreateStationRequest request = new CreateStationRequest("잠실");
        final Long saveStationId = stationService.saveStation(request);

        // when
        final StationResponse response = stationService.findByStationId(saveStationId);

        // then
        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(new StationResponse(
                        saveStationId, "잠실"
                ));
    }
}
