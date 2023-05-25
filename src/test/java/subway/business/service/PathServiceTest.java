package subway.business.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.business.domain.*;
import subway.business.domain.fare.DistanceFareStrategy;
import subway.business.service.dto.ShortestPathResponse;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;
import static subway.fixtures.station.StationFixture.강남역;
import static subway.fixtures.station.StationFixture.역삼역;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    @InjectMocks
    private PathService pathService;

    @Mock
    private DistanceFareStrategy distanceFareStrategy;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    @DisplayName("역과 역 사이의 최단 경로 정보를 가져온다.")
    @Test
    void shouldReturnShortestPathResponseWhenInputSourceStationIdAndDestStationId() {
        when(stationRepository.findById(1L)).thenReturn(강남역);
        when(stationRepository.findById(2L)).thenReturn(역삼역);
        List<Section> sections = List.of(new Section(강남역, 역삼역, 10));
        when(lineRepository.findAll()).thenReturn(List.of(new Line(new Name("2호선"), sections)));
        when(distanceFareStrategy.calculateFare(10)).thenReturn(1250);

        ShortestPathResponse shortestPathResponse = pathService.getShortestPath(1L, 2L);

        assertAll(
                () -> assertThat(shortestPathResponse.getStationNamesOfShortestPath()).isEqualTo(List.of("강남역", "역삼역")),
                () -> assertThat(shortestPathResponse.getTotalDistance()).isEqualTo(10),
                () -> assertThat(shortestPathResponse.getTotalFare()).isEqualTo(1250)
        );
    }
}
