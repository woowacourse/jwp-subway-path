package subway.domain.path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.domain.subway.Distance;
import subway.domain.subway.Section;
import subway.domain.subway.Station;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShortestDistancePathStrategyTest {

    private ShortestDistancePathStrategy pathStrategy;
    private List<Section> sections;

    @BeforeEach
    public void setup() {
        pathStrategy = new ShortestDistancePathStrategy();
        sections = Arrays.asList(
                new Section(1L, new Distance(10), new Station(1L, "Station A"), new Station(2L, "Station B"), 1L),
                new Section(2L, new Distance(30), new Station(2L, "Station B"), new Station(3L, "Station C"), 1L),
                new Section(3L, new Distance(30), new Station(1L, "Station A"), new Station(3L, "Station C"), 2L)
        );
    }

    @Test
    public void getPathAndDistance_returnsShortestPathAndDistance() {
        Long startStationId = 1L;
        Long targetStationId = 3L;

        Map.Entry<List<Long>, Distance> result = pathStrategy.getPathAndDistance(sections, startStationId, targetStationId);

        List<Long> expectedPath = Arrays.asList(1L, 3L);
        int expectedDistance = 30;

        assertEquals(expectedPath, result.getKey());
        assertEquals(expectedDistance, result.getValue().getDistance());
    }
}
