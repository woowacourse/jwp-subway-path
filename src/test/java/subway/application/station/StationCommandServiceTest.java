package subway.application.station;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import subway.adapter.in.web.station.dto.StationCreateRequest;
import subway.application.port.out.station.StationCommandPort;
import subway.application.port.out.station.StationQueryPort;
import subway.application.service.station.StationCommandService;
import subway.domain.Station;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class StationCommandServiceTest {
    private StationQueryPort stationQueryPort;
    private StationCommandService stationCommandService;

    @BeforeEach
    void setUp() {
        StationCommandPort stationCommandPort = Mockito.mock(StationCommandPort.class);
        stationQueryPort = Mockito.mock(StationQueryPort.class);
        stationCommandService = new StationCommandService(stationCommandPort, stationQueryPort);
    }

    @Test
    @DisplayName("저장된 역이 없으면 역을 정상적으로 만든다.")
    void createStation() {
        given(stationQueryPort.findByName(any()))
                .willReturn(Optional.empty());

        assertThatNoException().isThrownBy(
                () -> stationCommandService.createStation(new StationCreateRequest("비버"))
        );
    }

    @Test
    @DisplayName("존재하는 역이면 예외처리")
    void createStationException() {
        given(stationQueryPort.findByName(any()))
                .willReturn(Optional.of(new Station("라빈")));

        assertThatThrownBy(
                () -> stationCommandService.createStation(new StationCreateRequest("라빈"))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}