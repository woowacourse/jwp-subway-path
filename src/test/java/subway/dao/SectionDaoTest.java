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

    @DisplayName("Section을 저장한다.")
    @Test
    void save() {
        // given
        SectionEntity sectionEntity = new SectionEntity(1L, 2L, 1L, 10);

        // when
        sectionDao.batchSave(List.of(sectionEntity));
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(1L);

        // then
        assertThat(sectionEntities).hasSize(1);
    }

    @DisplayName("여러 Section을 저장한다.")
    @Test
    void batchSave() {
        // given
        List<SectionEntity> insertSections = List.of(
                new SectionEntity(1L, 2L, 1L, 10),
                new SectionEntity(2L, 3L, 1L, 10));

        // when
        sectionDao.batchSave(insertSections);
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(1L);

        // then
        assertThat(sectionEntities).hasSize(2);
    }

    @DisplayName("Line id를 입력받아 해당하는 Section Entity 를 반환한다.")
    @Test
    void findByLineId() {
        // given
        sectionDao.batchSave(List.of(
                new SectionEntity(1L, 2L, 1L, 10),
                new SectionEntity(2L, 3L, 1L, 20),
                new SectionEntity(3L, 4L, 1L, 30)
        ));

        // when
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(1L);

        // then
        assertThat(sectionEntities).hasSize(3);
    }

    @DisplayName("Line id를 입력받아 일치하는 Section 들을 모두 삭제한다.")
    @Test
    void deleteByLineId() {
        // given
        sectionDao.batchSave(List.of(
                new SectionEntity(1L, 2L, 1L, 10),
                new SectionEntity(2L, 3L, 1L, 20),
                new SectionEntity(3L, 4L, 1L, 30)
        ));

        // when
        int deleteRowNumber = sectionDao.deleteByLineId(1L);

        // then
        assertThat(deleteRowNumber).isEqualTo(3);
    }
}
