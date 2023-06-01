package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.dto.SectionStationResultMap;
import subway.domain.subway.Distance;
import subway.domain.subway.Section;
import subway.domain.subway.Station;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@Sql("classpath:/remove-section-line.sql")
@JdbcTest
class SectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private SectionDao sectionDao;
    private StationDao stationDao;

    @BeforeEach
    void init() {
        stationDao = new StationDao(jdbcTemplate, dataSource);
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
        save();
    }

    private void save() {
        Station station1 = new Station(1L, "잠실역1");
        Station station2 = new Station(2L, "잠실역2");
        Station station3 = new Station(3L, "잠실역3");
        Station station4 = new Station(4L, "잠실역4");
        Station station5 = new Station(5L, "잠실역5");
        Station station6 = new Station(6L, "잠실역6");

        stationDao.insert(station1);
        stationDao.insert(station2);
        stationDao.insert(station3);
        stationDao.insert(station4);
        stationDao.insert(station5);
        stationDao.insert(station6);

        sectionDao.insert(new Section(new Distance(3), station1, station2, 1L));
        sectionDao.insert(new Section(new Distance(3), station3, station4, 1L));
        sectionDao.insert(new Section(new Distance(3), station5, station6, 2L));
    }

    @Test
    void 구간을_저장한다() {
        // given
        Station station7 = new Station(5L, "잠실역7");
        Station station8 = new Station(6L, "잠실역8");
        final Section section = new Section(new Distance(3), station7, station8, 1L);

        // when
        final Long id = sectionDao.insert(section);

        // then
        assertThat(id).isNotNull();
    }

    @Test
    void LineId로_구간을_조회한다() {
        // when
        List<SectionStationResultMap> resultMaps = sectionDao.findAll();
        // then
        assertThat(resultMaps).hasSize(3);
    }

    @Test
    void 구간을_모두_조회한다() {
        // when
        List<SectionStationResultMap> resultMaps = sectionDao.findAllByLineId(1L);
        // then
        assertThat(resultMaps).hasSize(2);
    }

    @Test
    void 구간을_수정한다() {
        // given
        Station station7 = new Station(7L, "잠실역7");
        Station station8 = new Station(8L, "잠실역8");
        Station station9 = new Station(9L, "잠실역9");
        Station station10 = new Station(10L, "잠실역10");

        stationDao.insert(station7);
        stationDao.insert(station8);
        stationDao.insert(station9);
        stationDao.insert(station10);

        final Long targetId = sectionDao.insert(new Section(new Distance(1), station7, station8, 1L));
        final Section newSection = new Section(targetId, new Distance(3), station10, station9, 1L);

        // when
        sectionDao.update(newSection);

        // then
        assertAll(
                () -> assertThat(findById(targetId).getUpStationId()).isEqualTo(station10.getId()),
                () -> assertThat(findById(targetId).getDownStationId()).isEqualTo(station9.getId())
        );
    }

    @Test
    void 같은_구간이_존재하면_예외가_발생한다() {
        // given
        Station station1 = new Station(1L, "잠실역1");
        Station station2 = new Station(2L, "잠실역2");
        sectionDao.insert(new Section(new Distance(3), station1, station2, 1L));

        // when, then
        assertAll(
                () -> assertThat(sectionDao.exists(1L, 2L, 1L)).isTrue(),
                () -> assertThat(sectionDao.exists(2L, 1L, 2L)).isFalse()
        );
    }

    @Test
    void 구간을_삭제한다() {
        // when
        sectionDao.deleteById(1L);

        // then
        assertThatThrownBy(() -> findById(1L))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void 라인_아이디의_구간을_모두_삭제한다() {
        // when
        sectionDao.deleteByLineId(1L);

        // then
        assertThat(sectionDao.findAllByLineId(1L)).hasSize(0);
    }

    @Test
    void 받은_구간들을_모두_저장한다() {
        Station station7 = new Station(7L, "잠실역7");
        Station station8 = new Station(8L, "잠실역8");
        Station station9 = new Station(9L, "잠실역9");

        stationDao.insert(station7);
        stationDao.insert(station8);
        stationDao.insert(station9);

        Section section1 = new Section(new Distance(3), station7, station8, 2L);
        Section section2 = new Section(new Distance(3), station8, station9, 2L);

        List<Section> sections = new ArrayList<>();
        sections.add(section1);
        sections.add(section2);

        sectionDao.saveAll(sections);

        assertThat(sectionDao.findAllByLineId(2L)).hasSize(3);
    }

    private SectionStationResultMap findById(Long sectionId) {
        final String sql = "SELECT se.id sectionId, " +
                "se.distance distance, " +
                "se.up_station_id upStationId, " +
                "st1.name upStationName, " +
                "se.down_station_id downStationId, " +
                "st2.name downStationName, " +
                "se.line_id lineId " +
                "FROM section se " +
                "JOIN station st1 ON st1.id = se.up_station_id " +
                "JOIN station st2 ON st2.id = se.down_station_id " +
                "WHERE se.id = ?";

        final RowMapper<SectionStationResultMap> rowMapper = (rs, num) -> new SectionStationResultMap(
                rs.getLong("sectionId"),
                rs.getInt("distance"),
                rs.getLong("upStationId"),
                rs.getString("upStationName"),
                rs.getLong("downStationId"),
                rs.getString("downStationName"),
                rs.getLong("lineId")
        );

        return jdbcTemplate.queryForObject(sql, rowMapper, sectionId);
    }

}
