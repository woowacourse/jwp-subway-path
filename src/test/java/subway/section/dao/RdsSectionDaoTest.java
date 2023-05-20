package subway.section.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.line.dao.RdsSectionDao;
import subway.domain.line.domain.Direction;
import subway.domain.line.entity.SectionEntity;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
class RdsSectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private RdsSectionDao rdsSectionDao;

    @Autowired
    void setUp(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        rdsSectionDao = new RdsSectionDao(jdbcTemplate, dataSource);
    }

    @DisplayName("구간을 저장한다.")
    @Test
    void insert() {
        final SectionEntity sectionEntity = rdsSectionDao.insert(new SectionEntity(null, 1L, 1L, 2L, 1));
        assertThat(sectionEntity.getId()).isPositive();
    }

    @DisplayName("lineId로 구간들을 검색한다.")
    @Test
    void findByLineId() {
        final List<SectionEntity> sectionEntities = rdsSectionDao.findByLineId(1L);
        assertAll(
                () -> assertThat(sectionEntities.get(0).getId()).isEqualTo(1L),
                () -> assertThat(sectionEntities.get(1).getId()).isEqualTo(2L)
        );
    }

    @DisplayName("lineId로 구간들을 검색한다.")
    @Test
    void findSectionsByLineId() {
        final List<SectionEntity> sectionDetailEntities = rdsSectionDao.findByLineId(1L);
        assertAll(
                () -> assertThat(sectionDetailEntities.get(0).getId()).isPositive(),
                () -> assertThat(sectionDetailEntities.get(0).getUpStationId()).isPositive(),
                () -> assertThat(sectionDetailEntities.get(0).getDownStationId()).isPositive(),
                () -> assertThat(sectionDetailEntities.get(1).getId()).isPositive(),
                () -> assertThat(sectionDetailEntities.get(1).getUpStationId()).isPositive(),
                () -> assertThat(sectionDetailEntities.get(1).getDownStationId()).isPositive()
        );
    }

    @DisplayName("id로 구간을 삭제한다.")
    @Test
    void deleteById() {
        rdsSectionDao.deleteById(1L);
        Integer result = jdbcTemplate.queryForObject("select count(*) from section where id = ?", Integer.class, 1L);
        assertThat(result).isZero();
    }


    @DisplayName("아랫 방향에 인접한 역이 존재하면 그 역을 반환한다.")
    @Test
    void findNeighborStationDown() {
        final Optional<SectionEntity> section = rdsSectionDao.findNeighborSection(1L, 1L, Direction.DOWN);
        assertAll(
                () -> assertThat(section).isPresent(),
                () -> assertThat(section.get().getId()).isEqualTo(1L)
        );
    }

    @DisplayName("특정 방향에 인접한 역이 존재하면 그 역을 반환한다.")
    @Test
    void findNeighborDownStation() {
        final Optional<SectionEntity> section = rdsSectionDao.findNeighborSection(1L, 1L, Direction.DOWN);
        assertAll(
                () -> assertThat(section).isPresent(),
                () -> assertThat(section.get().getId()).isEqualTo(1L)
        );
    }

    @DisplayName("윗 방향에 인접한 역이 존재하면 그 역을 반환한다.")
    @Test
    void findNeighborStationUp() {
        final Optional<SectionEntity> section = rdsSectionDao.findNeighborSection(1L, 2L, Direction.UP);
        assertAll(
                () -> assertThat(section).isPresent(),
                () -> assertThat(section.get().getId()).isEqualTo(1L)
        );
    }

    @DisplayName("특정 방향에 인접한 역이 존재하면 그 역을 반환한다.")
    @Test
    void findNeighborUpStation() {
        final Optional<SectionEntity> section = rdsSectionDao.findNeighborSection(1L, 2L, Direction.UP);
        assertAll(
                () -> assertThat(section).isPresent(),
                () -> assertThat(section.get().getId()).isEqualTo(1L)
        );
    }

    @DisplayName("특정 방향에 인접한 역이 존재하지 않으면 Optional.empty 를 반환한다.")
    @Test
    void findNeighborStationFail() {
        final Optional<SectionEntity> section = rdsSectionDao.findNeighborSection(1L, 1L, Direction.UP);
        assertThat(section).isEmpty();
    }
}
