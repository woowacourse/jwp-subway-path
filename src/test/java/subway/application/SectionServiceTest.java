package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.SectionDao;
import subway.dto.SectionRequest;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
class SectionServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private SectionService sectionService;

    @BeforeEach
    void init() {
        final SectionDao sectionDao = new SectionDao(jdbcTemplate, dataSource);
        sectionService = new SectionService(sectionDao);
    }
    @Test
    void 같은_역_두_개를_등록할_수_없다() {
        // given
        final SectionRequest request = new SectionRequest(3, 1L, 1L, 1L);

        // when, then
        assertThatThrownBy(() -> sectionService.insertSection(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
