package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.Fixture;
import subway.domain.Line;
import subway.domain.LineRepository;
import subway.dto.PathResponse;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @InjectMocks
    private PathService pathService;
    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationService stationService;

    @Test
    @DisplayName("최단 경로 및 거리, 요금 반환 기능")
    void findShortestPath() {
        // given
        final List<Line> lines = List.of(Fixture.lineABC, Fixture.lineBDE);
        given(lineRepository.findLines()).willReturn(lines);
        given(stationService.findStationById(1L)).willReturn(Fixture.stationA);
        given(stationService.findStationById(5L)).willReturn(Fixture.stationE);

        // when
        final PathResponse actual = pathService.findShortestPath(1L, 5L);

        // then
        assertAll(
                () -> assertThat(actual.getPathStations()).containsExactly("A", "B", "D", "E"),
                () -> assertThat(actual.getDistance()).isEqualTo(30),
                () -> assertThat(actual.getFee()).isEqualTo(1650)
        );
    }
}
