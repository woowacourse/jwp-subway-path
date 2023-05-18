package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.common.fixture.EntityFixture.일호선_남색_Entity;
import static subway.common.fixture.EntityFixture.후추_디노_Entity;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
@Sql("classpath:test_data.sql")
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
    void 구간_정보를_삽입한다() {
        //when
        final SectionEntity insertedSection = sectionDao.insert(후추_디노_Entity);

        //then
        assertSoftly(softly -> {
            softly.assertThat(insertedSection.getId()).isEqualTo(1L);
            softly.assertThat(insertedSection.getFromId()).isEqualTo(1L);
            softly.assertThat(insertedSection.getToId()).isEqualTo(2L);
            softly.assertThat(insertedSection.getDistance()).isEqualTo(7);
            softly.assertThat(insertedSection.getLineId()).isEqualTo(1L);
        });
    }

    @Test
    void 호선_id로_구간_정보를_조회한다() {
        //given
        final Long lineId = 일호선_남색_Entity.getId();
        sectionDao.insert(new SectionEntity(1L, 2L, 7, lineId));

        //when
        final List<SectionEntity> sections = sectionDao.findSectionsByLineId(lineId);

        //then
        assertSoftly(softly -> {
            softly.assertThat(sections).hasSize(1);
            final SectionEntity sectionEntity = sections.get(0);
            softly.assertThat(sectionEntity.getId()).isEqualTo(1L);
            softly.assertThat(sectionEntity.getFromId()).isEqualTo(1L);
            softly.assertThat(sectionEntity.getToId()).isEqualTo(2L);
            softly.assertThat(sectionEntity.getDistance()).isEqualTo(7);
            softly.assertThat(sectionEntity.getLineId()).isEqualTo(1L);
        });
    }

    @Test
    void id로_구간_정보를_삭제한다() {
        //given
        final Long id = sectionDao.insert(후추_디노_Entity).getId();

        //when
        sectionDao.deleteById(id);

        //then
        assertThatThrownBy(() -> sectionDao.findById(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 구간입니다.");
    }

    @Test
    void id로_구간_정보를_조회한다() {
        //given
        final Long id = sectionDao.insert(후추_디노_Entity).getId();

        //when
        final SectionEntity sectionEntity = sectionDao.findById(id);

        //then
        assertSoftly(softly -> {
            softly.assertThat(sectionEntity.getId()).isEqualTo(1L);
            softly.assertThat(sectionEntity.getFromId()).isEqualTo(1L);
            softly.assertThat(sectionEntity.getToId()).isEqualTo(2L);
            softly.assertThat(sectionEntity.getDistance()).isEqualTo(7);
            softly.assertThat(sectionEntity.getLineId()).isEqualTo(1L);
        });
    }

    @Test
    void id로_구간_정보를_조회할_때_구간_정보가_존재하지_않으면_예외를_던진다() {
        //given
        final Long wrongId = Long.MIN_VALUE;

        //expect
        assertThatThrownBy(() -> sectionDao.findById(wrongId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 구간입니다.");
    }
}
