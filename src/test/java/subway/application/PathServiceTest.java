package subway.application;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.dto.PathRequest;
import subway.dto.PathResponse;

@JdbcTest
class PathServiceTest {

    private final String startStation = "성수역";
    private final String endStation = "건대입구역";

    private StationDao stationDao;
    private LineDao lineDao;
    private PathService pathService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate, dataSource);
        lineDao = new LineDao(jdbcTemplate, dataSource);
        pathService = new PathService(stationDao, lineDao);
    }

    @Test
    @DisplayName("findPath()를 호출하면 최적의 경로 정보를 반환한다")
    void findPath_success() {
        // given
        PathRequest request = new PathRequest(startStation, endStation);
        List<String> expectedPath = List.of("성수역", "뚝섬역", "잠실역", "건대입구역");
        int expectedDistance = 26;
        int expectedCost = 1650;
        PathResponse expected = new PathResponse(expectedPath, expectedDistance, expectedCost);

        // when
        PathResponse actual = pathService.findPath(request);

        // then
        Assertions.assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(expected);

    }

    @Test
    @DisplayName("findPath()를 호출할 때 존재하지 않는 역의 정보가 포함되어 있으면 예외를 반환한다.")
    void findPath_fail_not_exist_station() {
        // given
        String notExistStation = "없는역";
        PathRequest request= new PathRequest(notExistStation, endStation);

        // when, then
        Assertions.assertThatThrownBy(() -> pathService.findPath(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 역이 포함되어 있습니다");
    }

    @Test
    @DisplayName("findPath()를 호출할 때 출발역과 도착역이 동일하다면 예외를 반환한다.")
    void findPath_fail_same_station() {
        // given
        PathRequest request = new PathRequest(endStation, endStation);
        // when, then
        Assertions.assertThatThrownBy(() ->pathService.findPath(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("출발역과 도착역이 같습니다");
    }
}