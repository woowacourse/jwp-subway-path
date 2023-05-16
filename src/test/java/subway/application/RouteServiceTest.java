package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static subway.fixture.LineFixture.잠실_신림_이동_가능한_구간들;
import static subway.fixture.StationFixture.신림역;
import static subway.fixture.StationFixture.잠실역;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.application.dto.RouteRequest;
import subway.application.dto.RouteResponse;
import subway.application.dto.StationResponse;
import subway.domain.fare.Fare;
import subway.domain.line.LineRepository;
import subway.domain.station.StationRepository;

@ExtendWith(MockitoExtension.class)
class RouteServiceTest {

    @Mock
    private FareCalculator fareCalculator;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private RouteService routeService;

    @Test
    @DisplayName("요청받은 출발역에서 도착역까지 도달하는데 사용되는 최소 비용의 구간 및 비용 정보를 반환한다.")
    void getShortestRouteAndFare() {
        // given
        doReturn(잠실역)
            .when(stationRepository).findById(1L);
        doReturn(신림역)
            .when(stationRepository).findById(5L);
        when(lineRepository.getPossibleSections(anyLong(), anyLong()))
            .thenReturn(잠실_신림_이동_가능한_구간들());
        when(fareCalculator.calculateFare(any()))
            .thenReturn(new Fare(1550));

        final RouteRequest routeRequest = new RouteRequest(1L, 5L);

        // when
        final RouteResponse routeResponse = routeService.getShortestRouteAndFare(routeRequest);

        // then
        assertThat(routeResponse.getStations())
            .extracting(StationResponse::getId, StationResponse::getName)
            .containsExactly(
                tuple(1L, "잠실역"),
                tuple(2L, "선릉역"),
                tuple(6L, "남위례역"),
                tuple(4L, "신림역"));

        assertThat(routeResponse)
            .extracting(RouteResponse::getFare)
            .isEqualTo(1550);
    }
}
