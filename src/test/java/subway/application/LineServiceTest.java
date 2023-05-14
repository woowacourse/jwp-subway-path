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
import subway.dto.LineResponse;
import subway.entity.StationEntity;

@JdbcTest
class LineServiceTest {

    Long lineId;


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

    @Test
    @DisplayName("findLineResponses()를 호출하면 존재하는 모든 노선의 정보를 반환한다.")
    void findLineResponses() {
        // given
        int expectSize = 1;

        // when
        List<LineResponse> lines = lineService.findLineResponses();

        // then
        assertAll(
            () -> Assertions.assertThat(lines.size()).isEqualTo(expectSize),
            () -> Assertions.assertThat(lines.get(0).getId()).isEqualTo(1L),
            () -> Assertions.assertThat(lines.get(0).getName()).isEqualTo("1호선"),
            () -> Assertions.assertThat(lines.get(0).getColor()).isEqualTo("파란색")
        );
    }

    @Test
    @DisplayName("findLineResponseById()를 호출하면 특정 노선의 정보를 반환한다.")
    void findLineResponseById() {
        // given, when
        LineResponse line = lineService.findLineResponseById(lineId);

        // then
        assertAll(
            () -> Assertions.assertThat(line.getId()).isEqualTo(1L),
            () -> Assertions.assertThat(line.getName()).isEqualTo("1호선"),
            () -> Assertions.assertThat(line.getColor()).isEqualTo("파란색")
        );
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
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 노선입니다."),
            () -> Assertions.assertThat(afterStations).isEmpty()
        );
    }
}