package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.domain.LineFixture.FIXTURE_LINE_1;
import static subway.domain.SectionFixture.SECTION_START;
import static subway.domain.StationFixture.FIXTURE_STATION_1;
import static subway.domain.StationFixture.FIXTURE_STATION_2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql({"classpath:/schema.sql"})
class SectionDaoTest {

    private final SectionDao sectionDao;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public SectionDaoTest(final JdbcTemplate jdbcTemplate) {
        this.sectionDao = new SectionDao(jdbcTemplate);
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("insert into line (name, color) VALUES"
                + " ('" + FIXTURE_LINE_1.getName() + "', '" + FIXTURE_LINE_1.getColor() + "');");
        jdbcTemplate.update("insert into station (name) VALUES ('" + FIXTURE_STATION_1.getName() + "');");
        jdbcTemplate.update("insert into station (name) VALUES ('" + FIXTURE_STATION_2.getName() + "');");
    }

    @DisplayName("구간을 저장할 수 있다.")
    @Test
    void insert() {
        sectionDao.insert(FIXTURE_LINE_1, SECTION_START);

        assertThat(sectionDao.findByLineId(FIXTURE_LINE_1.getId()))
                .containsExactlyInAnyOrder(SECTION_START);
    }

    @DisplayName("구간을 삭제할 수 있다.")
    @Test
    void delete() {
        sectionDao.insert(FIXTURE_LINE_1, SECTION_START);
        sectionDao.deleteById(1L, 1L);

        assertThat(sectionDao.findAll()).isEmpty();
    }
}
