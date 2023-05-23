package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import javax.sql.DataSource;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.line.Line;

@JdbcTest
class RdsLineDaoTest {

    private RdsLineDao rdsLineDao;

    @Autowired
    void setUp(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        rdsLineDao = new RdsLineDao(jdbcTemplate, dataSource);
    }

    @DisplayName("노선을 저장하면 id와 함께 노선을 반환한다.")
    @Test
    void insert() {
        final Line result = rdsLineDao.insert(new Line("2호선", "초록색"));
        assertAll(
                () -> assertThat(result.getId()).isPositive(),
                () -> assertThat(result.getName()).isEqualTo("2호선"),
                () -> assertThat(result.getColor()).isEqualTo("초록색")
        );
    }

    @DisplayName("모든 노선을 조회한다.")
    @Test
    @Sql({"classpath:line.sql"})
    void findAll() {
        final List<Line> result = rdsLineDao.findAll();
        assertThat(result).containsExactly(
                new Line(1L, null, null),
                new Line(2L, null, null)
        );
    }

    @DisplayName("id로 노선을 조회한다.")
    @Test
    void findById() {
        final Line line = rdsLineDao.insert(new Line("1호선", "파란색"));
        final Long lineId = line.getId();
        final Line result = rdsLineDao.findById(lineId);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(lineId),
                () -> assertThat(result.getName()).isEqualTo("1호선"),
                () -> assertThat(result.getColor()).isEqualTo("파란색")
        );
    }

    @DisplayName("노선을 업데이트 한다.")
    @Test
    void update() {
        final Line line = rdsLineDao.insert(new Line("1호선", "파란색"));
        final Long lineId = line.getId();
        rdsLineDao.update(new Line(lineId, "2호선", "초록색"));
        final Line result = rdsLineDao.findById(lineId);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(lineId),
                () -> assertThat(result.getName()).isEqualTo("2호선"),
                () -> assertThat(result.getColor()).isEqualTo("초록색")
        );
    }

    @DisplayName("노선을 삭제한다.")
    @Test
    void deleteById() {
        final Line line = rdsLineDao.insert(new Line("1호선", "파란색"));
        final Long lineId = line.getId();
        assertThatCode(() -> rdsLineDao.deleteById(lineId))
                .doesNotThrowAnyException();
    }
}
