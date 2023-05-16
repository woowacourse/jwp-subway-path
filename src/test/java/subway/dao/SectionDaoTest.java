package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.domain.LineFixture.FIXTURE_LINE_1;
import static subway.domain.SectionFixture.SECTION_MIDDLE_1;
import static subway.domain.SectionFixture.SECTION_START;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

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
        sectionDao.insertAllByLineId(FIXTURE_LINE_1.getId(), List.of(SECTION_START));

        assertThat(sectionDao.findByLineId(FIXTURE_LINE_1.getId()))
                .containsExactlyInAnyOrder(SECTION_START);
    }

    @DisplayName("노선에 해당하는 구간 목록을 조회할 수 있다.")
    @Test
    void findByLineId() {
        sectionDao.insertAllByLineId(FIXTURE_LINE_1.getId(), List.of(SECTION_START, SECTION_MIDDLE_1));

        assertThat(sectionDao.findByLineId(FIXTURE_LINE_1.getId()))
                .containsExactlyInAnyOrder(SECTION_START, SECTION_MIDDLE_1);
    }

    @DisplayName("노선에 해당하는 구간을 모두 삭제할 수 있다.")
    @Test
    void deleteByLineId() {
        sectionDao.insertAllByLineId(FIXTURE_LINE_1.getId(), List.of(SECTION_START));

        sectionDao.deleteByLineId(FIXTURE_LINE_1.getId());

        assertThat(sectionDao.findByLineId(FIXTURE_LINE_1.getId()))
                .isEmpty();
    }
}
