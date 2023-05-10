package subway.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.SectionEntity;

@JdbcTest
class SectionEntityDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;
    private SectionDao sectionDao;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
    }

    @Test
    @DisplayName("section 테이블 삽입")
    void line_station_insert() {
        // given
        SectionEntity sectionEntity = new SectionEntity(1L, 2L, 10);

        // when
        SectionEntity result = sectionDao.insert(sectionEntity);

        // then
        assertThat(result.getLeftStationId()).isEqualTo(sectionEntity.getLeftStationId());
        assertThat(result.getRightStationId()).isEqualTo(sectionEntity.getRightStationId());
        assertThat(result.getDistance()).isEqualTo(sectionEntity.getDistance());
    }

}