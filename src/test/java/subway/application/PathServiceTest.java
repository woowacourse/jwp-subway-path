package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.application.costpolicy.CostPolicyChain;
import subway.controller.dto.response.FindShortestPathResponse;
import subway.controller.dto.response.StationInformationResponse;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.vo.Age;
import subway.domain.vo.Distance;
import subway.persistence.LineRepository;
import subway.persistence.StationRepository;

@ExtendWith(MockitoExtension.class)
class
PathServiceTest {

    private final Station firstStation = new Station("firstStation");
    private final Station secondStation = new Station("secondStation");
    private final Station thirdStation = new Station("thirdStation");
    private final Station fourthStation = new Station("fourthStation");
    private final Section section1 = new Section(firstStation, secondStation, new Distance(1L));
    private final Section section2 = new Section(secondStation, thirdStation, new Distance(1L));
    private final Section section3 = new Section(thirdStation, fourthStation, new Distance(1L));
    private final Section section4 = new Section(secondStation, firstStation, new Distance(10L));
    private final Section section5 = new Section(firstStation, thirdStation, new Distance(11L));
    private final Section section6 = new Section(thirdStation, fourthStation, new Distance(12L));
    private final int age = 10;

    @Mock
    private LineRepository lineRepository;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private CostPolicyChain costPolicyChain;
    @InjectMocks
    private PathService pathService;

    @Test
    @DisplayName("출발점부터 도착점까지 최단 경로를 찾는다.")
    void testFindShortestPath() {
        //given
        final Sections sections1 = new Sections(new ArrayList<>(List.of(section1, section2, section3)));
        final Line line1 = new Line("lineName", "lineColor", 0L, sections1);
        final Sections sections2 = new Sections(new ArrayList<>(List.of(section4, section5, section6)));
        final Line line2 = new Line("lineName", "lineColor", 0L, sections2);

        given(stationRepository.findByName(anyString()))
            .willReturn(Optional.of(firstStation), Optional.of(fourthStation));
        given(lineRepository.findAll())
            .willReturn(List.of(line1, line2));
        given(costPolicyChain.calculate(any(), any(Age.class), anyLong()))
            .willReturn(1250L);

        //when
        final FindShortestPathResponse response = pathService.findShortestPath(firstStation.getName(),
            fourthStation.getName(), age);

        //then
        final List<StationInformationResponse> stationResponses = response.getStationInformations();
        final List<Station> expectedStations = List.of(firstStation, secondStation, thirdStation, fourthStation);
        for (int index = 0; index < stationResponses.size(); index++) {
            assertThat(stationResponses.get(index))
                .extracting("stationId", "stationName", "lineId", "lineName", "lineColor")
                .containsExactly(expectedStations.get(index).getId(), expectedStations.get(index).getName(),
                    line1.getId(), line1.getName(), line1.getColor()
                );
        }
        assertThat(response.getTotalCost()).isEqualTo(1250L);
        assertThat(response.getTotalDistance()).isEqualTo(3L);
    }
}
