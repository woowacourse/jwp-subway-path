package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import subway.dao.entity.SectionEntity;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class SectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private SectionDao sectionDao;

    @BeforeEach
    void init() {
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
        save();
    }

    private void save() {
        sectionDao.insert(new SectionEntity(3, 1L, 2L, 1L));
        sectionDao.insert(new SectionEntity(3, 3L, 4L, 1L));
        sectionDao.insert(new SectionEntity(3, 5L, 6L, 2L));
    }

    @Test
    void 구간을_저장한다() {
        // given
        final SectionEntity sectionEntity = new SectionEntity(3, 1L, 2L, 1L);

        // when
        final Long id = sectionDao.insert(sectionEntity);

        // then
        assertThat(id).isNotNull();
    }

    @Test
    void LineId로_구간을_조회한다() {
        // when
        final List<SectionEntity> sections = sectionDao.findAllByLineId(1L);

        // then
        assertThat(sections).hasSize(2);
    }

    @Test
    void 구간을_수정한다() {
        // given
        final Long targetId = sectionDao.insert(new SectionEntity(1, 7L, 6L, 1L));
        final SectionEntity newSection = new SectionEntity(targetId, 3, 10L, 9L, 1L);

        // when
        sectionDao.update(newSection);

        // then
        assertAll(
                () -> assertThat(findById(targetId).getUpStationId()).isEqualTo(10L),
                () -> assertThat(findById(targetId).getDownStationId()).isEqualTo(9L)
        );
    }

    @Test
    void 같은_구간이_존재하면_예외가_발생한다() {
        // given
        sectionDao.insert(new SectionEntity(3, 1L, 3L, 1L));

        // when, then
        assertAll(
                () -> assertThat(sectionDao.exists(1L, 3L)).isTrue(),
                () -> assertThat(sectionDao.exists(3L, 1L)).isFalse()
        );
    }

    @Test
    void 구간을_삭제한다() {
        // given
        final Long id = sectionDao.insert(new SectionEntity(3, 3L, 11L, 1L));

        // when
        sectionDao.delete(id);

        // then
        assertThatThrownBy(() -> findById(id))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    private SectionEntity findById(Long sectionId) {
        final String sql = "SELECT * FROM section where id = ?";

        final RowMapper<SectionEntity> rowMapper = (rs, num) -> new SectionEntity(
                rs.getLong("id"),
                rs.getInt("distance"),
                rs.getLong("up_station_id"),
                rs.getLong("down_station_id"),
                rs.getLong("line_id")
        );

        return jdbcTemplate.queryForObject(sql, rowMapper, sectionId);
    }
}
