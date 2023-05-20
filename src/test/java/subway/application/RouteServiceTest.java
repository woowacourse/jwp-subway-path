package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static subway.exception.ErrorCode.ROUTE_NOT_EXISTS;
import static subway.fixture.LineFixture.잠실_신림_이동_가능한_구간들;
import static subway.fixture.StationFixture.산성역;
import static subway.fixture.StationFixture.신림역;
import static subway.fixture.StationFixture.잠실역;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.application.dto.FareResponse;
import subway.application.dto.RouteResponse;
import subway.application.dto.StationResponse;
import subway.domain.line.LineRepository;
import subway.domain.station.StationRepository;
import subway.exception.BadRequestException;

@ExtendWith(MockitoExtension.class)
class RouteServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private RouteService routeService;

    @Test
    @DisplayName("요청받은 출발역에서 도착역까지 도달하는데 사용되는 최소 비용의 구간 및 연령별 비용 정보를 계산하여 반환한다.")
    void getShortestRouteAndFare_success() {
        // given
        doReturn(잠실역)
            .when(stationRepository).findById(1L);
        doReturn(신림역)
            .when(stationRepository).findById(4L);
        when(lineRepository.getPossibleSections(anyLong(), anyLong()))
            .thenReturn(잠실_신림_이동_가능한_구간들());

        // when
        final RouteResponse routeResponse = routeService.getShortestRouteAndFare(1L, 4L);

        // then
        assertThat(routeResponse.getStations())
            .extracting(StationResponse::getId, StationResponse::getName)
            .containsExactly(
                tuple(1L, "잠실역"),
                tuple(2L, "선릉역"),
                tuple(6L, "남위례역"),
                tuple(4L, "신림역"));

        assertThat(routeResponse.getFare())
            .extracting(FareResponse::getNormalFare, FareResponse::getTeenagerFare, FareResponse::getChildFare)
            .containsExactly(1450, 880, 550);
    }

    @Test
    @DisplayName("요청받은 출발역에서 도착역까지 도달하는데 사용되는 최소 비용의 구간 및 연령별 비용 정보를 계산하여 반환한다. (하행에서 상행으로 이동 고려)")
    void getShortestRouteAndFare_reversed_success() {
        // given
        doReturn(잠실역)
            .when(stationRepository).findById(1L);
        doReturn(신림역)
            .when(stationRepository).findById(4L);
        when(lineRepository.getPossibleSections(anyLong(), anyLong()))
            .thenReturn(잠실_신림_이동_가능한_구간들());

        // when
        final RouteResponse routeResponse = routeService.getShortestRouteAndFare(4L, 1L);

        // then
        assertThat(routeResponse.getStations())
            .extracting(StationResponse::getId, StationResponse::getName)
            .containsExactly(
                tuple(4L, "신림역"),
                tuple(6L, "남위례역"),
                tuple(2L, "선릉역"),
                tuple(1L, "잠실역"));

        assertThat(routeResponse.getFare())
            .extracting(FareResponse::getNormalFare, FareResponse::getTeenagerFare, FareResponse::getChildFare)
            .containsExactly(1450, 880, 550);
    }

    @Test
    @DisplayName("목적지로 이동할 수 없는 경우 예외가 발생한다.")
    void getShortestRouteAndFare_fail() {
        // given
        doReturn(잠실역)
            .when(stationRepository).findById(1L);
        doReturn(산성역)
            .when(stationRepository).findById(7L);
        when(lineRepository.getPossibleSections(anyLong(), anyLong()))
            .thenReturn(잠실_신림_이동_가능한_구간들());

        // expected
        assertThatThrownBy(() -> routeService.getShortestRouteAndFare(1L, 7L))
            .isInstanceOf(BadRequestException.class)
            .extracting("errorCode")
            .isEqualTo(ROUTE_NOT_EXISTS);
    }
}