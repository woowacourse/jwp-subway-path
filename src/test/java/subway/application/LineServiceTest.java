package subway.application;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;
import javax.sql.DataSource;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.entity.StationEntity;

@Sql({"/schema-test.sql", "/data-test.sql"})
@JdbcTest
class LineServiceTest {

    private Long lineId;

    private StationDao stationDao;
    private LineDao lineDao;
    private LineService lineService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate, dataSource);
        lineDao = new LineDao(jdbcTemplate, dataSource);
        lineService = new LineService(stationDao, lineDao);
        lineId = 1L;
    }

    @Nested
    @DisplayName("saveLine()을 호출할 때")
    class saveLine {

        private String newLineName = "수인분당선";
        private String upEndStationName = "왕십리";
        private String downEndStationName = "인천";
        private int distance = 100;

        @Test
        @DisplayName("새로운 노선과 역들을 추가한다.")
        void success() {
            // given
            LineRequest request = new LineRequest(newLineName, "노란색", 300,upEndStationName,
                downEndStationName, distance);
            int beforeSize = lineService.findLineResponses().size();

            // when
            Long newLineId = lineService.saveLine(request);
            int afterSize = lineService.findLineResponses().size();
            int distanceOfUpStation = stationDao.findByLineIdAndName(newLineId, upEndStationName)
                .getDistance();
            boolean isUpEndStation = lineDao.isUpEndStation(newLineId, upEndStationName);
            boolean isDownEndStation = stationDao.isDownEndStation(newLineId, downEndStationName);

            // then
            assertAll(
                () -> Assertions.assertThatCode(() -> lineDao.findById(newLineId))
                    .doesNotThrowAnyException(),
                () -> Assertions.assertThat(afterSize).isEqualTo(beforeSize + 1),
                () -> Assertions.assertThat(distanceOfUpStation).isEqualTo(distance),
                () -> Assertions.assertThat(isUpEndStation).isTrue(),
                () -> Assertions.assertThat(isDownEndStation).isTrue()
            );
        }

        @Test
        @DisplayName("이미 같은 이름의 노선이 존재한다면 예외를 발생시킨다")
        void saveLine_fail_same_name() {
            // given
            newLineName = "1호선";
            LineRequest request = new LineRequest(newLineName, "노란색", 0,upEndStationName,
                downEndStationName, distance);

            // when, then
            Assertions.assertThatThrownBy(() -> lineService.saveLine(request))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessage("이미 같은 이름의 노선이 존재합니다");
        }

        @Test
        @DisplayName("상행역과 하행역의 이름이 동일하다면 예외를 발생시킨다")
        void saveLine_fail_same_stations() {
            // given
            downEndStationName = "왕십리";
            LineRequest request = new LineRequest(newLineName, "노란색", 300,upEndStationName,
                downEndStationName, distance);

            // when, then
            Assertions.assertThatThrownBy(() -> lineService.saveLine(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상행역과 하행역은 같은 이름을 가질 수 없습니다.");
        }

        @Test
        @DisplayName("상행역과 하행역 사이의 거리가 양의 정수가 아니라면 예외를 발생시킨다")
        void saveLine_fail_not_positive_distance() {
            // given
            distance = 0;
            LineRequest request = new LineRequest(newLineName, "노란색", 300,upEndStationName,
                downEndStationName, distance);

            // when, then
            Assertions.assertThatThrownBy(() -> lineService.saveLine(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 양의 정수여야 합니다");
        }
    }

    @Test
    @DisplayName("findLineResponses()를 호출하면 존재하는 모든 노선의 정보를 반환한다.")
    void findLineResponses() {
        // given
        int expectSize = 3;
        LineResponse expected=new LineResponse(1L, "1호선","파란색",0);

        // when
        List<LineResponse> lines = lineService.findLineResponses();

        // then
        assertAll(
            ()->Assertions.assertThat(lines.get(0)).usingRecursiveComparison().isEqualTo(expected),
            () -> Assertions.assertThat(lines.size()).isEqualTo(expectSize)
        );
    }

    @Test
    @DisplayName("findLineResponseById()를 호출하면 특정 노선의 정보를 반환한다.")
    void findLineResponseById() {
        // given
        LineResponse expected=new LineResponse(1L, "1호선","파란색",0);
        // when
        LineResponse actual = lineService.findLineResponseById(lineId);

        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    @DisplayName("deleteLineById()를 호출하면 특정 노선과 그 노선에 포함된 역들을 모두 삭제한다")
    void deleteLineById() {
        // given, when
        lineService.deleteLineById(lineId);
        List<StationEntity> afterStations = stationDao.findByLineId(lineId);

        // then
        assertAll(
            () -> Assertions.assertThatThrownBy(() -> lineService.findLineResponseById(lineId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("존재하지 않는 노선입니다."),
            () -> Assertions.assertThat(afterStations).isEmpty()
        );
    }
}
