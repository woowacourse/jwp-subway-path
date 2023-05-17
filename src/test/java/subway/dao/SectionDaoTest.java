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
import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.domain.station.Station;

@JdbcTest
@Sql("/line_data_initialize.sql")
class SectionDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    SectionDao sectionDao;

    private long lineId = 1L;

    @BeforeEach
    void setUp() {
        this.sectionDao = new MySqlSectionDao(jdbcTemplate);
    }

    @Test
    @DisplayName("노선의 구간 갯수가 정확하게 반환되어야 한다.")
    void countByLineId_success() {
        // given
        sectionDao.insert(lineId, new Section(new Station("잠실역"), new Station("삼성역"), new Distance(1)));
        sectionDao.insert(lineId, new Section(new Station("삼성역"), new Station("부산역"), new Distance(1)));
        sectionDao.insert(2L, new Section(new Station("삼성역"), new Station("부산역"), new Distance(1)));

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
        long sectionId = sectionDao.insert(lineId,
                new Section(new Station("잠실역"), new Station("삼성역"), new Distance(1)));

        // when
        sectionDao.update(sectionId, new Section(new Station("삼성역"), new Station("부산역"), new Distance(1)));

        List<SectionEntity> sections = sectionDao.findAllByLineId(lineId);

        List<SectionEntity> sectionEntities = List.of(
                new SectionEntity(sectionId, lineId, new Station("삼성역"), new Station("부산역"), 1));
        assertThat(sections).usingRecursiveComparison()
                .isEqualTo(sectionEntities);
    }

    @ParameterizedTest
    @CsvSource(value = {"잠실역:true", "부산역:false", "삼성역:true"}, delimiter = ':')
    @DisplayName("주어진 역이 구간에 있는지 확인한다.")
    void existsByStartStationNameAndLineId(String stationName, boolean exists) {
        // given
        sectionDao.insert(lineId, new Section(new Station("잠실역"), new Station("삼성역"), new Distance(1)));

        // when
        boolean expect = sectionDao.isStationInLine(lineId, stationName);

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
        sectionDao.insert(lineId, new Section(new Station("잠실역"), new Station("삼성역"), new Distance(1)));

        // expect
        assertThat(sectionDao.isEmptyByLineId(lineId))
                .isFalse();
    }
}
