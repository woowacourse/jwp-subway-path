package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;
import static subway.fixture.LineFixture.사호선;
import static subway.fixture.LineFixture.삼호선;
import static subway.fixture.LineFixture.이호선;
import static subway.fixture.LineFixture.일호선;
import static subway.fixture.StationFixture.강남역;
import static subway.fixture.StationFixture.선릉역;
import static subway.fixture.StationFixture.시청역;
import static subway.fixture.StationFixture.용산역;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.controller.dto.RouteSearchResponse;
import subway.controller.dto.StationResponse;
import subway.domain.line.Line;

@ExtendWith(MockitoExtension.class)
class SubwayServiceTest {

    @Mock
    private LineService lineService;

    @Mock
    private StationService stationService;

    @InjectMocks
    private SubwayService subwayService;

    @Test
    @DisplayName("역과 역 사이의 경로를 찾는다.")
    void findRoute() {
        String startStationName = "강남역";
        String endStationName = "선릉역";
        List<Line> lines = List.of(일호선, 이호선, 삼호선, 사호선);
        given(lineService.findAllLines()).willReturn(lines);
        given(stationService.findByName(startStationName)).willReturn(강남역);
        given(stationService.findByName(endStationName)).willReturn(선릉역);
        StationResponse response1 = StationResponse.of(강남역);
        StationResponse response2 = StationResponse.of(용산역);
        StationResponse response3 = StationResponse.of(시청역);
        StationResponse response4 = StationResponse.of(선릉역);

        RouteSearchResponse routeSearchResponse = subwayService.findRoute(startStationName, endStationName);

        assertAll(
                () -> assertThat(routeSearchResponse.getRoutes()).usingRecursiveComparison()
                        .isEqualTo(List.of(response1, response2, response3,
                                response4)),
                () -> assertThat(routeSearchResponse.getDistance()).isEqualTo(20),
                () -> assertThat(routeSearchResponse.getFare()).isEqualTo(1450)
        );
    }
}
