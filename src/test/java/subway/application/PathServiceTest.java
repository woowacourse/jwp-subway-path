package subway.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Section;
import subway.domain.Station;
import subway.integration.IntegrationTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql("/setUpStationPath.sql")
class PathServiceTest extends IntegrationTest {

    @Autowired
    PathService pathService;

    //given
    //   (1)   (2)   (3)
    // 1  -  2  -  3 - 7
    //       |         | (10)
    //       4-5-6 -8 -9
    @Test
    void findShortestPath_when_no_transfer_success() {
        List<Section> shortestPath = pathService.findShortestPath(1L, 3L);
        List<Long> pathUpStations = shortestPath.stream().map(Section::getUpStation).map(Station::getId).collect(Collectors.toList());

        assertThat(shortestPath.size()).isEqualTo(2);
        Assertions.assertThat(pathUpStations).containsExactly(1L, 2L);
    }

    @Test
    void findShortestPath_when_transfer_line_success() {
        List<Section> shortestPath = pathService.findShortestPath(1L, 9L);

        assertThat(shortestPath.size()).isEqualTo(4);
        List<Long> pathUpStations = shortestPath.stream().map(Section::getUpStation).map(Station::getId).collect(Collectors.toList());

        Assertions.assertThat(pathUpStations).containsExactly(1L, 2L, 3L, 7L);
    }
}
