package subway.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dto.SectionRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(SectionDao.class)
class SectionDaoTest {

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("section 테이블에 값을 추가한다.")
    @Test
    void insert() {
        // given
        SectionRequest sectionRequest = new SectionRequest(2L, 1L, 1L, 1);

        // when
        Long sectionId = sectionDao.insert(sectionRequest);

        // then
        assertThat(sectionId).isEqualTo(1L);
    }

    @DisplayName("section 테이블에서 stationId에 해당하는 값을 삭제한다.")
    @Test
    void deleteByStationId() {
        // given
        Long stationId = 2L;
        SectionRequest sectionRequest = new SectionRequest(stationId, 1L, 1L, 1);
        Long sectionId = sectionDao.insert(sectionRequest);

        // when
        sectionDao.deleteByStationId(stationId);

        // then
        assertThat(findById(sectionId).isEmpty()).isTrue();
    }

    private Optional<Long> findById(final Long id) {
        String sql = "SELECT * FROM section WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Long.class, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
