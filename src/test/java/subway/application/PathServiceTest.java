package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import subway.dao.SectionDao;
import subway.domain.fee.FeeStrategy;
import subway.domain.path.PathStrategy;
import subway.domain.subway.Distance;
import subway.domain.subway.Section;
import subway.domain.subway.Station;
import subway.dto.PathRequest;
import subway.dto.PathResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class PathServiceTest {

    private PathService pathService;

    @Mock
    private SectionDao sectionDao;

    @Mock
    private PathStrategy pathStrategy;

    @Mock
    private FeeStrategy feeStrategy;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        pathService = new PathService(sectionDao, pathStrategy, feeStrategy);
    }

    @Test
    public void getDijkstraShortestPath_returnsPathResponse() {
        // Mock sectionDao.findAll() to return a list of sections
        List<Section> sections = Arrays.asList(
                new Section(1L, new Distance(10), new Station(1L, "일번역"), new Station(2L, "이번역"), 1L),
                new Section(2L, new Distance(20), new Station(2L, "이번역"), new Station(3L, "삼번역"), 1L),
                new Section(3L, new Distance(30), new Station(3L, "삼번역"), new Station(4L, "사번역"), 1L)
        );
        List<Section> allSections = sectionDao.findAll();
        when(sectionDao.findAll()).thenReturn(sections);

        // Mock pathStrategy.getPathAndDistance() to return a map entry of path and distance
        List<Long> expectedPath = Arrays.asList(1L, 2L, 3L, 4L);
        Distance expectedDistance = new Distance(60);
        Map.Entry<List<Long>, Distance> pathAndDistance = Map.entry(expectedPath, expectedDistance);
        when(pathStrategy.getPathAndDistance(sections, 1L, 4L)).thenReturn(pathAndDistance);

        when(feeStrategy.calculateFee(new Distance(60))).thenReturn(2150);

        // Create a PathRequest object
        PathRequest pathRequest = new PathRequest(1L, 4L);

        // Call the method under test
        PathResponse pathResponse = pathService.getPath(pathRequest);

        // Verify the result
        assertThat(expectedPath).isEqualTo(pathResponse.getShortestPath());
        assertThat(expectedDistance.getDistance()).isEqualTo(pathResponse.getDistance());
        assertThat(pathResponse.getFee()).isEqualTo(2150);
    }

    // Add more tests for other methods in PathService if needed
}
