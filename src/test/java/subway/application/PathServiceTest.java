package subway.application;

import java.util.List;
import java.util.NoSuchElementException;
import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.dto.PathRequest;
import subway.dto.PathResponse;

@Sql({"/schema-test.sql", "/data-test.sql"})
@JdbcTest
class PathServiceTest {

    private static final String NoPo = "노포역";
    private static final String SeongSu = "성수역";
    private static final String KeonDae = "건대입구역";
    private static final String GangNam = "강남역";

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
        PathRequest request = new PathRequest(SeongSu, KeonDae);
        List<String> expectedPath = List.of("성수역", "뚝섬역", "잠실역", "건대입구역");
        int expectedDistance = 25;
        int expectedCharge = 1850;
        int teenagerCharge = 1200;
        int childCharge = 750;
        PathResponse expected = new PathResponse(expectedPath, expectedDistance, expectedCharge,
            teenagerCharge, childCharge);

        // when
        PathResponse actual = pathService.findPath(request);

        // then
        Assertions.assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(expected);

    }

    @Test
    @DisplayName("findPath()를 호출할 떄 하나의 노선만 거친다면 그 노선의 할증 금액만큼 할증된다.")
    void findPath_success_one_line() {
        // given
        PathRequest request = new PathRequest(GangNam, KeonDae);
        List<String> expectedPath = List.of("강남역", "역삼역", "선릉역", "삼성역", "건대입구역");
        int expectedDistance = 33;
        int expectedCharge = 1750;
        int teenagerCharge = 1120;
        int childCharge = 700;
        PathResponse expected = new PathResponse(expectedPath, expectedDistance, expectedCharge,
            teenagerCharge, childCharge);

        // when
        PathResponse actual = pathService.findPath(request);

        // then
        Assertions.assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(expected);

    }

    @Test
    @DisplayName("findPath()를 호출하면 최적의 경로 정보를 반환한다")
    void findPath_success_over_fifty() {
        // given
        PathRequest request = new PathRequest(GangNam, SeongSu);
        List<String> expectedPath = List.of("강남역", "역삼역", "선릉역", "삼성역", "잠실역", "뚝섬역", "성수역");
        int expectedDistance = 55;
        int expectedCharge = 2450;
        int teenagerCharge = 1680;
        int childCharge = 1050;
        PathResponse expected = new PathResponse(expectedPath, expectedDistance, expectedCharge,
            teenagerCharge, childCharge);

        // when
        PathResponse actual = pathService.findPath(request);

        // then
        Assertions.assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(expected);

    }

    @Test
    @DisplayName("findPath()를 호출할 때 출발역과 도착역이 서로 도달할 수 없는 위치에 있다면 예외를 반환한다")
    void findPath_fail() {
        // given
        PathRequest request = new PathRequest(NoPo, GangNam);

        // when, then
        Assertions.assertThatThrownBy(() -> pathService.findPath(request))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("두 역은 연결되어 있지 않습니다");

    }

    @Test
    @DisplayName("findPath()를 호출할 때 존재하지 않는 역의 정보가 포함되어 있으면 예외를 반환한다.")
    void findPath_fail_not_exist_station() {
        // given
        String notExistStation = "없는역";
        PathRequest request = new PathRequest(notExistStation, KeonDae);

        // when, then
        Assertions.assertThatThrownBy(() -> pathService.findPath(request))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("존재하지 않는 역이 포함되어 있습니다");
    }

    @Test
    @DisplayName("findPath()를 호출할 때 출발역과 도착역이 동일하다면 예외를 반환한다.")
    void findPath_fail_same_station() {
        // given
        PathRequest request = new PathRequest(KeonDae, KeonDae);
        // when, then
        Assertions.assertThatThrownBy(() -> pathService.findPath(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("출발역과 도착역이 같습니다");
    }
}
