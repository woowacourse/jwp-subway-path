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
    private final Long lineId = 1L;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SectionDao sectionDao;
    private SectionEntity sectionEntity;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate);
        sectionEntity = new SectionEntity(1L, 2L, lineId, 10);
    }

    @Test
    @DisplayName("SectionEntity를 입력받아 저장한다.")
    void save() {
        // when
        sectionDao.save(sectionEntity);
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineId);

        // expected
        assertThat(sectionEntities).hasSize(1);
    }

    @Test
    @DisplayName("Line id를 입력받아 해당하는 Section Entity 를 반환한다.")
    void findById() {
        // given
        sectionDao.save(sectionEntity);
        sectionDao.save(new SectionEntity(2L, 3L, 1L, 20));
        sectionDao.save(new SectionEntity(3L, 4L, 1L, 30));

        // when
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineId);

        // expected
        assertThat(sectionEntities).hasSize(3);
    }

    @Test
    @DisplayName("Section Entity 를 입력받아 일치하는 Section 을 삭제한다.")
    void deleteByName() {
        // when
        sectionDao.save(sectionEntity);
        int deleteRowNumber = sectionDao.delete(sectionEntity);

        // expected
        assertThat(deleteRowNumber).isEqualTo(1);
    }

    @Test
    @DisplayName("Line id를 입력받아 일치하는 Section 들을 모두 삭제한다.")
    void deleteByLineId() {
        // given
        sectionDao.save(sectionEntity);
        sectionDao.save(new SectionEntity(2L, 3L, 1L, 20));
        sectionDao.save(new SectionEntity(3L, 4L, 1L, 30));

        // when
        int deleteRowNumber = sectionDao.deleteByLineId(1L);

        // expected
        assertThat(deleteRowNumber).isEqualTo(3);
    }
}
