package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.entity.LineEntity;
import subway.dao.entity.LineWithSectionEntities;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class LineDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
    }

    @Test
    void LineEntity를_입력받아_저장한다() {
        // given
        LineEntity 노선 = new LineEntity("2호선");

        // when
        Long 노선_ID = lineDao.save(노선);
        LineEntity 찾은_노선 = lineDao.findById(노선_ID);

        // expected
        assertThat(찾은_노선.getName()).isEqualTo(노선.getName());
    }

    @Test
    void 노선_이름을_입력받아_해당하는_Entity를_반환한다() {
        // given
        LineEntity 노선 = new LineEntity("2호선");
        Long 노선_ID = lineDao.save(노선);

        // when
        LineEntity 찾은_노선 = lineDao.findByName("2호선");

        // expected
        assertThat(찾은_노선.getId()).isEqualTo(노선_ID);
    }

    @Test
    void 노선_id를_입력받아_해당하는_Entity를_반환한다() {
        // given
        Long 노선_ID = lineDao.save(new LineEntity("2호선"));

        // when
        LineEntity 찾은_노선 = lineDao.findById(노선_ID);

        // expected
        assertThat(찾은_노선.getId()).isEqualTo(노선_ID);
    }

    @Test
    void 모든_노선을_반환한다() {
        // given
        lineDao.save(new LineEntity("2호선"));
        lineDao.save(new LineEntity("3호선"));
        lineDao.save(new LineEntity("4호선"));
        lineDao.save(new LineEntity("5호선"));

        // when
        List<LineWithSectionEntities> 노선과_노선_내부의_구간들 = lineDao.findLinesWithSections();

        // expected
        assertThat(노선과_노선_내부의_구간들).hasSize(4);
    }

    @Test
    void 노선_id를_입력받아_일치하는_노선을_삭제한다() {
        // given
        Long 노선_ID = lineDao.save(new LineEntity("2호선"));

        // when
        int 삭제된_노선_개수 = lineDao.deleteById(노선_ID);

        // expected
        assertThat(삭제된_노선_개수).isEqualTo(1);
    }
}
