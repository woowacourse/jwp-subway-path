package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.dto.SectionRequest;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Sql("classpath:/remove-section-line.sql")
@JdbcTest
class SectionServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private SectionService sectionService;
    private SectionDao sectionDao;
    private LineDao lineDao;

    // 1 -> 2 -> 3
    @BeforeEach
    void init() {
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
        lineDao = new LineDao(jdbcTemplate, dataSource);
        sectionService = new SectionService(sectionDao, lineDao);
        initialSection();
    }

    private void initialSection() {
        lineDao.insert(new Line("2호선", "초록색"));
        sectionDao.insert(new Section(10,1L,2L,1L));
        sectionDao.insert(new Section(20,2L,3L,1L));
    }

    @Test
    void 하행역_종점에_구간을_추가한다() {
        // 3->4 추가
        final Long id = sectionService.insertSection(new SectionRequest(3, 3L, 4L, 1L));

        // 1 -> 2 -> 3 -> 4
        assertThat(id).isNotNull();
    }

    @Test
    void 상행역_종점에_구간을_추가한다() {
        // 4 -> 1 추가
        final Long id = sectionService.insertSection(new SectionRequest(3, 4L, 1L, 1L));

        // 4 -> 1 -> 2 -> 3
        assertThat(id).isNotNull();
    }

    @Test
    void 중간에_추가하는데_상행역_기준인_경우() {
        // 1 -> 4
        final Long id = sectionService.insertSection(new SectionRequest(3, 1L, 4L, 1L));

        // 1 -> 4 -> 2 -> 3
        assertThat(id).isNotNull();
    }

    @Test
    void 중간에_추가하는데_하행역_기준인_경우() {
        // 4 -> 2
        final Long id = sectionService.insertSection(new SectionRequest(3, 4L, 2L, 1L));

        // 1 -> 4 -> 2 -> 3
        assertThat(id).isNotNull();
    }

    @Test
    void 같은_역_두_개를_등록할_수_없다() {
        // given
        final SectionRequest request = new SectionRequest(3, 1L, 1L, 1L);

        // when, then
        assertThatThrownBy(() -> sectionService.insertSection(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 동일한_구간은_등록할_수_없다() {
        // given
        final SectionRequest request = new SectionRequest(3, 1L, 2L, 1L);

        // when, then
        assertThatThrownBy(() -> sectionService.insertSection(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 역이_존재하지_않으면_추가할_수_있다() {
        // given
        final SectionRequest request = new SectionRequest(3, 50L, 50L, 1L);

        // when, then
        assertThatThrownBy(() -> sectionService.insertSection(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 추가하려는_거리는_기존_거리보다_클_수_없다() {
        // given
        final SectionRequest request = new SectionRequest(100, 2L, 4L, 1L);

        // when, then
        assertThatThrownBy(() -> sectionService.insertSection(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 노선이_없으면_추가할_수_없다() {
        // given
        final SectionRequest request = new SectionRequest(100, 2L, 4L, 3L);

        // when, then
        assertThatThrownBy(() -> sectionService.insertSection(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
