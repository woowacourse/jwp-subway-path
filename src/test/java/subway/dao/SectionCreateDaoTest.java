package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.entity.SectionEntity;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.fixture.LineFixture.SECOND_LINE;
import static subway.fixture.StationFixture.GANGNAM;
import static subway.fixture.StationFixture.JAMSIL;
import static subway.fixture.StationFixture.SEONLEUNG;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class SectionCreateDaoTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SectionDao sectionDao;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
    }

    @Test
    void 새로운_섹션을_저장한다() {
        SectionEntity sectionEntity = new SectionEntity(JAMSIL.getId(), SEONLEUNG.getId(), 10, SECOND_LINE.getId());

        SectionEntity savedSection = sectionDao.insert(sectionEntity);

        assertAll(
                () -> assertThat(savedSection.getId()).isPositive(),
                () -> assertThat(savedSection.getUpStationId()).isEqualTo(JAMSIL.getId()),
                () -> assertThat(savedSection.getDownStationId()).isEqualTo(SEONLEUNG.getId()),
                () -> assertThat(savedSection.getDistance()).isEqualTo(10)
        );
    }

    @Test
    void 섹션_조회() {
        SectionEntity upSectionEntity = new SectionEntity(JAMSIL.getId(), SEONLEUNG.getId(), 10, SECOND_LINE.getId());
        SectionEntity downSectionEntity = new SectionEntity(SEONLEUNG.getId(), GANGNAM.getId(), 3, SECOND_LINE.getId());

        sectionDao.insert(upSectionEntity);
        sectionDao.insert(downSectionEntity);

        sectionDao.findSectionsByLine(SECOND_LINE.getId());
    }
}
