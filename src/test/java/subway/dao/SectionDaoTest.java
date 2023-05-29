package subway.dao;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static subway.fixture.SectionFixture.SECTION_강남_잠실_5;
import static subway.fixture.SectionFixture.SECTION_몽촌토성_암사_5;
import static subway.fixture.SectionFixture.SECTION_잠실_몽촌토성_5;
import static subway.fixture.SectionVoFixture.SECTION_VO_강남_잠실_5;
import static subway.fixture.SectionVoFixture.SECTION_VO_몽촌토성_암사_5;
import static subway.fixture.SectionVoFixture.SECTION_VO_잠실_몽촌토성_5;
import static subway.fixture.StationFixture.STATION_강남;
import static subway.fixture.StationFixture.STATION_몽촌토성;
import static subway.fixture.StationFixture.STATION_암사;
import static subway.fixture.StationFixture.STATION_잠실;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.entity.LineEntity;
import subway.entity.vo.SectionVo;

@JdbcTest
@Sql("/schema.sql")
class SectionDaoTest {
    private LineDao lineDao;
    private StationDao stationDao;
    private SectionDao sectionDao;

    private long lineId;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
        stationDao = new StationDao(jdbcTemplate);
        sectionDao = new SectionDao(jdbcTemplate);

        LineEntity lineEntity = lineDao.insert(new Line("1호선", "blue"));
        lineId = lineEntity.getId();

        stationDao.insert(STATION_강남);
        stationDao.insert(STATION_잠실);
        stationDao.insert(STATION_몽촌토성);
        stationDao.insert(STATION_암사);
    }

    @Test
    @DisplayName("노선에 구간들을 등록한다")
    void insertSections() {
        // given
        int previousSize = sectionDao.findSectionsByLineId(lineId).size();
        List<Section> sections = List.of(SECTION_강남_잠실_5, SECTION_잠실_몽촌토성_5);

        // when
        sectionDao.insertSections(sections, lineId);

        // then
        assertThat(sectionDao.findSectionsByLineId(lineId))
                .hasSize(previousSize + 2);
    }

    @Test
    @DisplayName("노선에 속하는 모든 구간들을 조회한다")
    void findSectionsByLineId() {
        // given
        List<Section> sections = List.of(SECTION_강남_잠실_5, SECTION_잠실_몽촌토성_5);
        sectionDao.insertSections(sections, lineId);
        List<SectionVo> insertedSections = sectionDao.findSectionsByLineId(lineId);

        // when, then
        assertThat(sectionDao.findSectionsByLineId(lineId))
                .hasSize(2);
    }

    @Test
    @DisplayName("구간들을 삭제한다.")
    void deleteSections() {
        // given
        List<Section> sections = List.of(SECTION_강남_잠실_5, SECTION_잠실_몽촌토성_5);
        sectionDao.insertSections(sections, lineId);
        int preSize = sectionDao.findSectionsByLineId(lineId).size();

        // when
        List<Section> sections2 = List.of(SECTION_강남_잠실_5);
        sectionDao.deleteSections(sections2, lineId);

        // then
        assertThat(sectionDao.findSectionsByLineId(lineId))
                .hasSize(preSize - 1);
    }

    @Test
    @DisplayName("모든 구간들을 조회한다")
    void findAll() {
        // given
        List<Section> sections = List.of(SECTION_강남_잠실_5, SECTION_잠실_몽촌토성_5, SECTION_몽촌토성_암사_5);
        sectionDao.insertSections(sections, lineId);
        List<SectionVo> all = sectionDao.findAll();

        // when, then
        assertThat(all)
                .hasSize(sections.size())
                .contains(SECTION_VO_강남_잠실_5, SECTION_VO_잠실_몽촌토성_5, SECTION_VO_몽촌토성_암사_5);
    }
}
