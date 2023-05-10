package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Section;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class SectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private SectionDao sectionDao;

    @BeforeEach
    void init() {
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
    }

    @Test
    void 같은_구간이_존재하면_예외가_발생한다() {
        // given
        final Section section = new Section(3, 1L, 2L, 1L);
        sectionDao.insert(section);

        // when, then
        assertAll(
                () -> assertThat(sectionDao.exists(1L, 2L)).isTrue(),
                () -> assertThat(sectionDao.exists(2L, 1L)).isFalse()
        );
    }
}
