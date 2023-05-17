package subway.service;

import static fixtures.StationFixtures.GANGNAM;
import static fixtures.StationFixtures.GYODAE;
import static fixtures.StationFixtures.YANGJAE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.BDDMockito.given;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.controller.dto.request.PassengerRequest;
import subway.controller.dto.response.ShortestPathResponse;
import subway.domain.fare.FareStrategy;
import subway.domain.line.Line;
import subway.domain.section.PathSection;
import subway.domain.subway.Passenger;
import subway.domain.subway.Subway;
import subway.repository.LineRepository;
import subway.repository.StationRepository;

@ExtendWith(MockitoExtension.class)
class SubwayServiceTest {

    @InjectMocks
    private SubwayService subwayService;

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private FareStrategy fareStrategy;

    @Test
    @DisplayName("탑승자 정보를 바탕으로 경로 정보를 반환한다.")
    void findShortestPath() {
        final PassengerRequest request = new PassengerRequest(8, 1L, 3L);
        final Line lineOfOne = new Line(1L, "1호선", "빨간색", 500);
        final Line lineOfTwo = new Line(2L, "2호선", "파란색", 1000);
        lineOfOne.addSection(GANGNAM, YANGJAE, 5);
        lineOfTwo.addSection(YANGJAE, GYODAE, 7);

        given(lineRepository.findAll()).willReturn(List.of(lineOfOne, lineOfTwo));
        given(stationRepository.findById(1L)).willReturn(GANGNAM);
        given(stationRepository.findById(3L)).willReturn(GYODAE);
        given(fareStrategy.calculateFare(anyDouble(), any(Passenger.class), any(Subway.class))).willReturn(1000d);

        final ShortestPathResponse result = subwayService.findShortestPath(request);

        final ShortestPathResponse expected = ShortestPathResponse.of(
                List.of(
                        new PathSection(1L, GANGNAM, YANGJAE, 5, 500),
                        new PathSection(2L, YANGJAE, GYODAE, 7, 1000)
                ),
                12,
                1000
        );
        assertThat(result).usingRecursiveComparison().isEqualTo(expected);
    }
}
