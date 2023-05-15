package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.entity.SectionEntity;

@JdbcTest
@Sql("/section_initialize.sql")
class SectionDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    SectionDao sectionDao;

    private long lineId = 1L;

    @BeforeEach
    void setUp() {
        this.sectionDao = new SectionDao(jdbcTemplate);
    }

    @Test
    @DisplayName("노선의 구간 갯수가 정확하게 반환되어야 한다.")
    void countByLineId_success() {
        // given
        sectionDao.insert(new SectionEntity(lineId, 1L,2L, 1));
        sectionDao.insert(new SectionEntity(lineId, 2L, 3L, 1));

        // when
        Long count = sectionDao.countByLineId(lineId);

        // then
        assertThat(count)
                .isEqualTo(2L);
    }

    @Test
    @DisplayName("노선에 구간이 없으면 갯수가 0이어야 한다.")
    void countByLineId_emptySection() {
        // when
        Long count = sectionDao.countByLineId(lineId);

        // then
        assertThat(count)
                .isZero();
    }

    @Test
    @DisplayName("노선의 수정이 정상적으로 되어야 한다.")
    void update_success() {
        // given
        SectionEntity sectionEntity = new SectionEntity(lineId, 1L, 2L, 1);
        sectionDao.insert(sectionEntity);
        sectionEntity = sectionDao.findAllByLineId(lineId).get(0);
        sectionEntity.updateDistance(3);
        sectionEntity.updateEndStationId(3L);
        sectionEntity.updateStartStationId(2L);

        // when
        sectionDao.update(sectionEntity);

        List<SectionEntity> sections = sectionDao.findAllByLineId(lineId);

        List<SectionEntity> sectionEntities = List.of(sectionEntity);
        assertThat(sections).usingRecursiveComparison()
                .isEqualTo(sectionEntities);
    }

    @ParameterizedTest
    @CsvSource(value = {"1:true", "2:false", "3:true"}, delimiter = ':')
    @DisplayName("주어진 역이 구간에 있는지 확인한다.")
    void existsByStartStationNameAndLineId(Long stationId, boolean exists) {
        // given
        sectionDao.insert(new SectionEntity(lineId, 1L, 3L, 1));
        // when
        boolean expect = sectionDao.isStationInLineById(lineId, stationId);

        // then
        assertThat(expect)
                .isEqualTo(exists);
    }

    @Test
    @DisplayName("노선에 구간이 비어있는지 확인한다.")
    void isEmptyByLineId_true() {
        // expect
        assertThat(sectionDao.isEmptyByLineId(lineId))
                .isTrue();
    }

    @Test
    @DisplayName("노선에 구간이 있는지 확인한다.")
    void isEmptyByLineId_false() {
        // given
        sectionDao.insert(new SectionEntity(lineId, 1L, 2L, 1));

        // expect
        assertThat(sectionDao.isEmptyByLineId(lineId))
                .isFalse();
    }
}
