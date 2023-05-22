package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
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
import subway.domain.fare.DistanceFarePolicy;
import subway.domain.fare.Fare;
import subway.domain.line.Distance;
import subway.domain.line.Line;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
class SubwayServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private SubwayService subwayService;

    @Test
    @DisplayName("역과 역 사이의 경로를 찾는다.")
    void findRoute() {
        String startStationName = "강남역";
        String endStationName = "선릉역";
        List<Line> lines = List.of(일호선, 이호선, 삼호선, 사호선);
        given(lineRepository.findAll()).willReturn(lines);
        given(stationRepository.findByName(startStationName)).willReturn(강남역);
        given(stationRepository.findByName(endStationName)).willReturn(선릉역);
        given(DistanceFarePolicy.calculateFare(any(Distance.class))).willReturn(new Fare(1550));
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
                () -> assertThat(routeSearchResponse.getFare()).isEqualTo(1550)
        );

    }
}
