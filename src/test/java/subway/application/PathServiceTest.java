package subway.application;

import static fixtures.path.PathSectionDtoFixtures.INITIAL_PATH_RESPONSE;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.PathResponse;

@SpringBootTest
@Transactional
@Sql({"/test-schema.sql", "/test-path-data.sql"})
class PathServiceTest {

    @Autowired
    private PathService pathService;

    @Test
    @DisplayName("출발 노선, 역 이름과 도착 노선, 역 이름으로 최단 경로가 담긴 응답을 반환한다.")
    void findShortestPath() {
        // given
        String startLineName = "2호선";
        String startStationName = "A역";
        String endLineName = "3호선";
        String endStationName = "E역";

        PathResponse expectedResponse = INITIAL_PATH_RESPONSE.RESPONSE;

        // when
        PathResponse pathResponse =
                pathService.findShortestPath(startLineName, startStationName, endLineName, endStationName);

        // then
        assertThat(pathResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
    }
}
