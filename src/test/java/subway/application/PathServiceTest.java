package subway.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.PathDto;
import subway.integration.IntegrationTest;

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
        PathDto shortest = pathService.findShortest(1L, 3L);
        assertThat(shortest.getPath().size()).isEqualTo(3);
        assertThat(shortest.getDistance()).isEqualTo(3D);
    }

    @Test
    void findShortestPath_when_transfer_line_success() {
        PathDto shortest = pathService.findShortest(1L, 9L);
        assertThat(shortest.getPath().size()).isEqualTo(5);
        assertThat(shortest.getDistance()).isEqualTo(16D);
    }
}
