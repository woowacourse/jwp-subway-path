package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.entity.SectionEntity;
import subway.domain.Line;
import subway.dto.SectionDeleteRequest;
import subway.dto.SectionRequest;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@Sql("classpath:/remove-section-line.sql")
@JdbcTest
class SectionServiceTest {

    @Autowired
    private DataSource dataSource;

    private LineDao lineDao;
    private SectionDao sectionDao;
    private SectionService sectionService;

    // 1 -> 2 -> 3
    @BeforeEach
    void init() {
        sectionDao = new SectionDao(new JdbcTemplate(dataSource), dataSource);
        lineDao = new LineDao(new JdbcTemplate(dataSource), dataSource);
        sectionService = new SectionService(sectionDao, lineDao, new SectionMapper());
        initialSection();
    }

    private void initialSection() {
        lineDao.insert(new Line("2호선", "초록색"));
        sectionDao.insert(new SectionEntity(10, 1L, 2L, 1L));
        sectionDao.insert(new SectionEntity(20, 2L, 3L, 1L));
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

    @Test
    void 구간이_한개이면_구간과_노선이_모두_삭제된다() {
        //given
        final Long lineId = lineDao.insert(new Line("3호선", "노란색"));
        sectionDao.insert(new SectionEntity(10, 1L, 2L, lineId));

        //when
        sectionService.deleteStation(1L, new SectionDeleteRequest(lineId));

        //then
        assertAll(
                () -> assertThat(lineDao.findById(lineId)).isEmpty(),
                () -> assertThat(sectionDao.findAllByLineId(lineId)).isEmpty()
        );
    }

    @Test
    void 구간이_두개_이상이고_종점이면_해당구간만_삭제한다() {
        //when
        sectionService.deleteStation(1L, new SectionDeleteRequest(1L));

        //then
        assertThat(sectionDao.findAllByLineId(1L)).hasSize(1);
    }

    @Test
    void 구간이_두개_이상이고_중간이면_겹치는구간_삭제와_이어주기를_한다() {
        //when
        sectionService.deleteStation(2L, new SectionDeleteRequest(1L));

        //then
        final List<SectionEntity> sections = sectionDao.findAllByLineId(1L);
        assertAll(() -> assertThat(sections).hasSize(1),
                () -> assertThat(sections.get(0).getUpStationId()).isEqualTo(1L),
                () -> assertThat(sections.get(0).getDownStationId()).isEqualTo(3L),
                () -> assertThat(sections.get(0).getDistance()).isEqualTo(30)
        );
    }
}
