package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static subway.fixture.LineFixture.잠실_신림_이동_가능한_구간들;
import static subway.fixture.StationFixture.남위례역;
import static subway.fixture.StationFixture.선릉역;
import static subway.fixture.StationFixture.신림역;
import static subway.fixture.StationFixture.잠실역;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.application.dto.FareResponse;
import subway.application.dto.RouteResponse;
import subway.application.dto.StationResponse;
import subway.domain.fare.Fare;
import subway.domain.fare.DiscountFare;
import subway.domain.fare.discount.DiscountFarePolicyComposite;
import subway.domain.fare.normal.FarePolicyComposite;
import subway.domain.line.LineRepository;
import subway.domain.route.GraphProvider;
import subway.domain.station.StationRepository;

@ExtendWith(MockitoExtension.class)
class RouteServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private GraphProvider graphProvider;

    @Mock
    private FarePolicyComposite farePolicies;

    @Mock
    private DiscountFarePolicyComposite discountFarePolicies;

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
        when(graphProvider.getShortestPath(anyList(), any(), any()))
            .thenReturn(List.of(잠실역, 선릉역, 남위례역, 신림역));
        when(farePolicies.getTotalFare(any()))
            .thenReturn(new Fare(1450));
        when(discountFarePolicies.getDiscountFares(any()))
            .thenReturn(new DiscountFare(new Fare(550), new Fare(880)));

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
        when(graphProvider.getShortestPath(anyList(), any(), any()))
            .thenReturn(List.of(신림역, 남위례역, 선릉역, 잠실역));
        when(farePolicies.getTotalFare(any()))
            .thenReturn(new Fare(1450));
        when(discountFarePolicies.getDiscountFares(any()))
            .thenReturn(new DiscountFare(new Fare(550), new Fare(880)));

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
}
