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

    public static final Station station1 = new Station(1L, "잠실역");
    public static final Station station2 = new Station(2L, "잠실나루역");
    public static final Station station3 = new Station(3L, "숙대입구역");
    public static final Station station4 = new Station(4L, "암사역");
    public static final Line line1 = new Line("1호선", "파랑");

    private StationDao stationDao;
    private LineDao lineDao;
    private SectionDao sectionDao;
    private long lineId;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);
        lineDao = new LineDao(jdbcTemplate);
        sectionDao = new SectionDao(jdbcTemplate);

        insertStations(station1, station2, station3, station4);
        lineId = lineDao.insert(line1).getId();
    }

    @Test
    @DisplayName("노선에 속하는 모든 구간들을 조회한다")
    void findAllSectionByLineId() {
        // given
        Section section = new Section(station1, station2, 10, lineId);
        sectionDao.save(section, lineId);

        // when
        Sections sections = sectionDao.findSectionsByLineId(lineId);

        // then
        assertThat(sections.getSections().get(0)).usingRecursiveComparison()
                .comparingOnlyFields("upStation")
                .comparingOnlyFields("downStation").isEqualTo(section);
    }

    @Test
    @DisplayName("노선에 구간을 등록한다")
    void saveInitialSection() {
        // given
        Section section = new Section(station1, station2, 10);

        // when
        long id = sectionDao.save(section, 1L);

        // then
        assertThat(id).isEqualTo(1);
    }

    @Test
    @DisplayName("해당 구간의 다음 구간을 수정한다.")
    void updateNextSection() {
        // given
        Section nextSection = new Section(station2, station3, 10);
        Section newNextSection = new Section(station2, station4, 5);
        long nextSectionId = sectionDao.save(nextSection, lineId);
        long newNextSectionId = sectionDao.save(newNextSection, lineId);

        Section section = new Section(station1, station2, 10, nextSectionId);
        long sectionId = sectionDao.save(section, lineId);

        // when
        sectionDao.updateSectionNext(newNextSectionId, sectionId);

        // then
        assertThat(sectionDao.findById(sectionId).getNextSectionId()).isEqualTo(newNextSectionId);
    }

    @Test
    @DisplayName("구간의 정보를 업데이트한다.")
    void update() {
        // given
        Section section = new Section(station1, station3, 10);
        long sectionId = sectionDao.save(section, lineId);

        Section newSection = new Section(sectionId, station1, station3, 5);

        // when
        sectionDao.update(newSection);

        // then
        assertThat(sectionDao.findById(sectionId).getDistance()).isEqualTo(5);
    }

    @Test
    @DisplayName("구간의 상행 역이 같으면 삭제한다.")
    void deleteUpEnd() {
        // given
        Section section = new Section(station1, station3, 10);
        sectionDao.save(section, lineId);

        // when
        int previousSize = sectionDao.findSectionsByLineId(lineId).size();
        sectionDao.deleteSectionByUpStationId(station1.getId(), lineId);

        // then
        assertThat(sectionDao.findSectionsByLineId(lineId).size())
                .isEqualTo(previousSize - 1);
    }

    @Test
    @DisplayName("구간의 하행 역이 같으면 삭제한다.")
    void deleteDownEnd() {
        // given
        Section section = new Section(station1, station3, 10);
        sectionDao.save(section, lineId);

        // when
        int previousSize = sectionDao.findSectionsByLineId(lineId).size();
        sectionDao.deleteSectionByDownStationId(station3.getId(), lineId);

        // then
        assertThat(sectionDao.findSectionsByLineId(lineId).size())
                .isEqualTo(previousSize - 1);
    }

    @Test
    @DisplayName("특정 호선 내 구간 모두을 삭제한다.")
    void deleteByLineId() {
        // given
        Section section = new Section(station1, station3, 10);
        sectionDao.save(section, lineId);

        // when
        int previousSize = sectionDao.findSectionsByLineId(lineId).size();
        sectionDao.deleteAllByLineId(lineId);

        // then
        assertThat(sectionDao.findSectionsByLineId(lineId).size())
                .isEqualTo(previousSize - 1);
    }

    private void insertStations(Station...stations) {
        for (Station station : stations) {
            stationDao.insert(station);
        }
    }
}
