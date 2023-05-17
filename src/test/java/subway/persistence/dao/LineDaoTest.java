package subway.persistence.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import subway.domain.Line;
import subway.exception.LineNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineDaoTest extends DaoTest {

    @Test
    void 노선을_조회한다() {
        final Line line = lineDao.findById(1L);

        assertThat(line.getName()).isEqualTo("1호선");
    }

    @Test
    void 노선을_추가한다() {
        final Line insert = lineDao.insert(new Line("추가노선", "검정"));
        final Line line = lineDao.findById(insert.getId());

        Assertions.assertAll(
                () -> assertThat(line.getName()).isEqualTo("추가노선"),
                () -> assertThat(line.getColor()).isEqualTo("검정")
        );
    }

    @Test
    void 모든_노선을_조회한다() {
        final List<Line> lines = lineDao.findAll();

        assertThat(lines).hasSize(3);
    }

    @Test
    void 노선을_삭제한다() {
        lineDao.deleteById(1L);

        final List<Line> lines = lineDao.findAll();

        assertThat(lines).hasSize(2);
    }

    @Test
    void 없는_노선을_삭제한다() {
        assertThatThrownBy(() -> lineDao.deleteById(10L))
                .isInstanceOf(LineNotFoundException.class)
                .hasMessageContaining("존재하지 않는 노선입니다.");
    }
}
