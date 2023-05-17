package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.entity.SectionEntity;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class SectionDaoTest {
    private final Long lineId = 1L;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SectionDao sectionDao;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate);
    }

    @Test
    void SectionEntity를_입력받아_저장한다() {
        // given
        SectionEntity 구간 = new SectionEntity(1L, 2L, lineId, 10);

        // when
        sectionDao.save(구간);
        List<SectionEntity> 구간들 = sectionDao.findByLineId(lineId);

        // expected
        assertThat(구간들).hasSize(1);
    }

    @Test
    void 구간_id를_입력받아_해당하는_Entity를_반환한다() {
        // given
        SectionEntity 구간 = new SectionEntity(1L, 2L, lineId, 10);
        sectionDao.save(구간);
        sectionDao.save(new SectionEntity(2L, 3L, 1L, 20));
        sectionDao.save(new SectionEntity(3L, 4L, 1L, 30));

        // when
        List<SectionEntity> 구간들 = sectionDao.findByLineId(lineId);

        // expected
        assertThat(구간들).hasSize(3);
    }

    @Test
    @DisplayName("Section Entity 를 입력받아 일치하는 Section 을 삭제한다.")
    void deleteByName() {
        // given
        SectionEntity 구간 = new SectionEntity(1L, 2L, lineId, 10);
        sectionDao.save(구간);

        // when
        int 삭제된_행_개수 = sectionDao.delete(구간);

        // expected
        assertThat(삭제된_행_개수).isEqualTo(1);
    }

    @Test
    @DisplayName("Line id를 입력받아 일치하는 Section 들을 모두 삭제한다.")
    void deleteByLineId() {
        // given
        SectionEntity 구간 = new SectionEntity(1L, 2L, lineId, 10);
        sectionDao.save(구간);

        sectionDao.save(new SectionEntity(2L, 3L, 1L, 20));
        sectionDao.save(new SectionEntity(3L, 4L, 1L, 30));

        // when
        int 삭제된_행_개수 = sectionDao.deleteByLineId(1L);

        // expected
        assertThat(삭제된_행_개수).isEqualTo(3);
    }
}
