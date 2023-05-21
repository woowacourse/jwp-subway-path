package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.fare.FareCalculator;
import subway.domain.section.SectionRepository;
import subway.dto.PathRequest;
import subway.dto.PathResponse;

import static fixtures.PathFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @InjectMocks
    PathService pathService;

    @Mock
    SectionRepository sectionRepository;
    @Mock
    FareCalculator fareCalculator;

    @Test
    @DisplayName("최단거리 경로를 찾는다.")
    void findShortestPathTest() {
        // given
        PathRequest request = REQUEST_PATH_강변역_TO_성수역;
        when(sectionRepository.findAllSections()).thenReturn(ALL_SECTIONS);
        when(fareCalculator.calculate(DISTANCE_강변역_TO_성수역)).thenReturn(FARE_강변역_TO_성수역);

        // when
        PathResponse response = pathService.findShortestPath(request);

        // then
        assertThat(response).isEqualTo(RESPONSE_PATH_강변역_TO_성수역);
    }
}