package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.fixture.LineFixture.FIXTURE_LINE_1;
import static subway.fixture.SectionFixture.SECTION_ST1_ST2;
import static subway.fixture.SectionFixture.SECTION_ST2_ST3;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DisplayName("지하철 구간 DAO 테스트")
@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class SectionDaoTest {

    private final SectionDao sectionDao;

    @Autowired
    public SectionDaoTest(final JdbcTemplate jdbcTemplate) {
        this.sectionDao = new SectionDao(jdbcTemplate);
    }

    @DisplayName("노선에 해당하는 구간 목록을 저장할 수 있다.")
    @Test
    void insertAllByLineId() {
        sectionDao.insertAllByLineId(FIXTURE_LINE_1.getId(), List.of(SECTION_ST1_ST2));

        assertThat(sectionDao.findByLineId(FIXTURE_LINE_1.getId()))
                .containsExactlyInAnyOrder(SECTION_ST1_ST2);
    }

    @DisplayName("노선에 해당하는 구간 목록을 조회할 수 있다.")
    @Test
    void findByLineId() {
        sectionDao.insertAllByLineId(FIXTURE_LINE_1.getId(), List.of(SECTION_ST1_ST2,
                SECTION_ST2_ST3));

        assertThat(sectionDao.findByLineId(FIXTURE_LINE_1.getId()))
                .containsExactlyInAnyOrder(SECTION_ST1_ST2, SECTION_ST2_ST3);
    }

    @DisplayName("노선에 해당하는 구간을 모두 삭제할 수 있다.")
    @Test
    void deleteByLineId() {
        sectionDao.insertAllByLineId(FIXTURE_LINE_1.getId(), List.of(SECTION_ST1_ST2));

        sectionDao.deleteByLineId(FIXTURE_LINE_1.getId());

        assertThat(sectionDao.findByLineId(FIXTURE_LINE_1.getId()))
                .isEmpty();
    }
}
