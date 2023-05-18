package subway.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import subway.dto.response.PathResponse;
import subway.integration.IntegrationTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Sql("/setupStationPath.sql")
class PathServiceImplTest extends IntegrationTest {

    @Autowired
    PathService pathService;

    //given
    //   (1)   (2)   (3)
    // 1  -  2  -  3 - 7
    //       |         | (5)
    //       4-5-6 -8 -9
    @Test
    void findShortestPath_when_no_transfer_success() {
        PathResponse shortest = pathService.findShortest(1L, 3L);
        assertThat(shortest.getPath().size()).isEqualTo(3);
        assertThat(shortest.getMoney()).isEqualTo(3D);
    }

    @Test
    void findShortestPath_when_transfer_line_success() {
        PathResponse shortest = pathService.findShortest(1L, 9L);
        assertThat(shortest.getPath().size()).isEqualTo(5);
        assertThat(shortest.getMoney()).isEqualTo(11D);
    }
}
