package subway.dao;

import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.SectionEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
class SectionH2DaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SectionDao sectionDao;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionH2Dao(jdbcTemplate);
    }

    @Test
    void saveTest() {
        final SectionEntity sectionEntity = new SectionEntity(1L, 1L, 2L, 5);
        final SectionEntity savedSection = sectionDao.insert(sectionEntity);

        assertThat(savedSection.getId()).isGreaterThanOrEqualTo(1L);
    }

    @Test
    void findAllTest() {
        final SectionEntity sectionEntity1 = new SectionEntity(1L, 1L, 2L, 5);
        final SectionEntity sectionEntity2 = new SectionEntity(1L, 2L, 3L, 3);

        final SectionEntity savedSection1 = sectionDao.insert(sectionEntity1);
        final SectionEntity savedSection2 = sectionDao.insert(sectionEntity2);

        assertAll(
                () -> assertThat(sectionDao.findAll().size()).isEqualTo(2),
                () -> assertThat(sectionDao.findAll()).contains(savedSection1, savedSection2)
        );
    }

    @Test
    void deleteTest() {
        final SectionEntity sectionEntity1 = new SectionEntity(1L, 1L, 2L, 5);
        final SectionEntity sectionEntity2 = new SectionEntity(1L, 2L, 3L, 3);
        final SectionEntity savedSection2 = sectionDao.insert(sectionEntity2);

        sectionDao.delete(sectionEntity1.getLineId(), sectionEntity1.getUpStationId(), sectionEntity1.getDownStationId());

        assertAll(
                () -> AssertionsForClassTypes.assertThat(sectionDao.findAll().size()).isEqualTo(1),
                () -> assertThat(sectionDao.findAll()).contains(savedSection2)
        );
    }
}
