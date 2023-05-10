package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.entity.SectionEntity;

@JdbcTest
class SectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SectionDao sectionDao;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate);
    }

    @Test
    @DisplayName("lineId가 존재하지 않으면 빈 컬렉션이 반환된다.")
    void findByLineId_NotExistLineId() {
        // given
        final Long lineId = 1L;

        // when
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineId);

        // expected
        assertThat(sectionEntities).hasSize(0);
    }
}
