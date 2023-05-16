package subway.dao;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import subway.domain.Section;

@Sql("/InitializeTable.sql")
@JdbcTest
class SectionDaoTest {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    SectionDao sectionDao;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
    }

    @Test
    void insert() {
        StationDao stationDao = new StationDao(jdbcTemplate, dataSource);
        stationDao.findAll().forEach(station -> System.out.println(station.getId()));
        Section section = new Section(null, 3L, 4L, 2L, 5);
        Long id = sectionDao.insert(section);

        assertThat(id).isEqualTo(3L);
    }

    @Test
    void findAllSectionByLineId() {
        assertThat(sectionDao.findAllSectionByLineId(2L)).hasSize(2);
    }

    @Test
    void findById() {
        Section section = sectionDao.findById(2L);
        assertAll(
            () -> assertThat(section.getId()).isEqualTo(2L),
            () -> assertThat(section.getSourceStationId()).isEqualTo(2L),
            () -> assertThat(section.getTargetStationId()).isEqualTo(3L),
            () -> assertThat(section.getLineId()).isEqualTo(2L),
            () -> assertThat(section.getDistance()).isEqualTo(10)
        );
    }

    @Test
    void deleteById() {
        sectionDao.deleteById(2L);
        assertThat(sectionDao.findAllSectionByLineId(2L)).hasSize(1);
    }
}
