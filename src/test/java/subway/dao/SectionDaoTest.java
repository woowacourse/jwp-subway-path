package subway.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.LineStation;
import subway.entity.Section;

@JdbcTest
class SectionDaoTest {

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
        Section section = new Section(1L, 2L, 10);

        // when
        Section result = sectionDao.insert(section);

        // then
        assertThat(result.getLeftStationId()).isEqualTo(section.getLeftStationId());
        assertThat(result.getRightStationId()).isEqualTo(section.getRightStationId());
        assertThat(result.getDistance()).isEqualTo(section.getDistance());
    }

}