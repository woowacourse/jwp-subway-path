package subway.section.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.SectionDao;
import subway.entity.SectionEntity;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
class SectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SectionDao sectionDao;

    @Autowired
    void setUp(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
    }

    @DisplayName("구간을 저장한다.")
    @Test
    void insert() {
        final SectionEntity sectionEntity = sectionDao.insert(new SectionEntity(null, 1L, 1L, 2L, 1));
        assertThat(sectionEntity.getId()).isPositive();
    }

    @DisplayName("lineId로 구간들을 검색한다.")
    @Test
    void findByLineId() {
        final List<SectionEntity> sectionEntities = sectionDao.findByLineId(1L);
        assertAll(
                () -> assertThat(sectionEntities.get(0).getId()).isEqualTo(1L),
                () -> assertThat(sectionEntities.get(1).getId()).isEqualTo(2L)
        );
    }

    @DisplayName("lineId로 구간들을 검색한다.")
    @Test
    void findSectionsByLineId() {
        final List<SectionEntity> sectionDetailEntities = sectionDao.findByLineId(1L);
        assertAll(
                () -> assertThat(sectionDetailEntities.get(0).getId()).isPositive(),
                () -> assertThat(sectionDetailEntities.get(0).getUpStationId()).isPositive(),
                () -> assertThat(sectionDetailEntities.get(0).getDownStationId()).isPositive(),
                () -> assertThat(sectionDetailEntities.get(1).getId()).isPositive(),
                () -> assertThat(sectionDetailEntities.get(1).getUpStationId()).isPositive(),
                () -> assertThat(sectionDetailEntities.get(1).getDownStationId()).isPositive()
        );
    }

    @DisplayName("id로 구간을 삭제한다.")
    @Test
    void deleteById() {
        sectionDao.deleteById(1L);
        Integer result = jdbcTemplate.queryForObject("select count(*) from section where id = ?", Integer.class, 1L);
        assertThat(result).isZero();
    }
}
