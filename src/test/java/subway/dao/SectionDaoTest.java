package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;

@JdbcTest
@Sql("/schema.sql")
class SectionDaoTest {

    private StationDao stationDao;
    private LineDao lineDao;
    private SectionDao sectionDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);
        lineDao = new LineDao(jdbcTemplate);
        sectionDao = new SectionDao(jdbcTemplate);
    }

    @Test
    @DisplayName("노선에 속하는 모든 구간들을 조회한다")
    void findAllSectionByLineId() {
        Station st1 = new Station(1L, "역1");
        stationDao.insert(st1);
        Station st2 = new Station(2L, "역2");
        stationDao.insert(st2);
        Line li = lineDao.insert(new Line("1호선", "파랑"));
        Section section = new Section(st1, st2, 10, li.getId());
        sectionDao.save(section, li.getId());
        Sections sections = sectionDao.findSectionsByLineId(li.getId());
        assertThat(sections.getSections().get(0)).usingRecursiveComparison()
                .comparingOnlyFields("upStation")
                .comparingOnlyFields("downStation").isEqualTo(section);
    }

    @Test
    @DisplayName("노선에 최초로 구간을 등록한다")
    void saveInitialSection() {
        Section section = new Section(new Station(1L, "역"), new Station(2L, "암사역"), 10);
        long id = sectionDao.save(section, 1L);
        assertThat(id).isEqualTo(1);
    }

    @Test
    @DisplayName("노선 하행종점에 새로 구간을 등록한다.")
    void saveDownEndSection() {
        //given
        long lineId = 1L;
        Station station1 = new Station(1L, "역");
        Station station2 = new Station(2L, "암사역");
        Station station3 = new Station(2L, "숙대입구역");
        stationDao.insert(station1);
        stationDao.insert(station2);
        stationDao.insert(station3);
        Section section = new Section(station1, station2, 10);
        Section newSection = new Section(station2, station3, 10);

        //when
        long savedId = sectionDao.save(section, lineId);
        long newSectionId = sectionDao.save(newSection, lineId);
        sectionDao.updateNextSection(newSectionId, savedId);

        //then
        Sections sections = sectionDao.findSectionsByLineId(lineId);
        assertThat(sections.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("노선 상행종점에 새로 구간을 등록한다.")
    void saveUpEndSection() {
        //given
        long lineId = 1L;
        Station station1 = new Station(1L, "역");
        Station station2 = new Station(2L, "암사역");
        Station station3 = new Station(2L, "숙대입구역");
        stationDao.insert(station1);
        stationDao.insert(station2);
        stationDao.insert(station3);
        Section section = new Section(station1, station2, 10);
        long savedId = sectionDao.save(section, lineId);

        //when
        Section newSection = new Section(station3, station1, 10, savedId);
        sectionDao.save(newSection, lineId);

        //then
        Sections sections = sectionDao.findSectionsByLineId(lineId);
        assertThat(sections.size()).isEqualTo(2);
    }
}
