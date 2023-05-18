package subway.repository.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.repository.dao.DaoFixtures.GANGNAM_STATION;
import static subway.repository.dao.DaoFixtures.LINE_NO_1;
import static subway.repository.dao.DaoFixtures.LINE_NO_2;
import static subway.repository.dao.DaoFixtures.NAMYOUNG_STATION;
import static subway.repository.dao.DaoFixtures.SAMSEONG_STATION;
import static subway.repository.dao.DaoFixtures.SEOLLEUNG_STATION;
import static subway.repository.dao.DaoFixtures.SEOUL_STATION;
import static subway.repository.dao.DaoFixtures.YEOKSAM_STATION;
import static subway.repository.dao.DaoFixtures.YONGSAN_STATION;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class SectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SectionDao sectionDao;
    private LineDao lineDao;
    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
        stationDao = new StationDao(jdbcTemplate);
        sectionDao = new SectionDao(jdbcTemplate);
    }

    @Test
    void 구간을_저장한다() {
        // given
        LineEntity saveLine = lineDao.insert(LINE_NO_2);
        StationEntity source = stationDao.insert(GANGNAM_STATION);
        StationEntity target = stationDao.insert(YEOKSAM_STATION);

        // when
        sectionDao.insert(new SectionEntity(source.getId(), target.getId(), saveLine.getId(), 6));
        List<SectionEntity> saveSections = sectionDao.findByLineId(saveLine.getId());

        // then
        assertThat(saveSections).hasSize(1);
    }

    @Test
    void 여러개의_구간을_저장한다() {
        // given
        LineEntity saveLine = lineDao.insert(LINE_NO_2);
        final Long lineId = saveLine.getId();
        StationEntity station1 = stationDao.insert(GANGNAM_STATION);
        StationEntity station2 = stationDao.insert(YEOKSAM_STATION);
        StationEntity station3 = stationDao.insert(SEOLLEUNG_STATION);
        StationEntity station4 = stationDao.insert(SAMSEONG_STATION);
        final SectionEntity section1 = new SectionEntity(station1.getId(), station2.getId(), lineId, 5);
        final SectionEntity section2 = new SectionEntity(station2.getId(), station3.getId(), lineId, 6);
        final SectionEntity section3 = new SectionEntity(station3.getId(), station4.getId(), lineId, 7);
        final List<SectionEntity> sections = List.of(section1, section2, section3);

        // when
        sectionDao.insertAll(sections);
        final List<SectionEntity> findSections = sectionDao.findAll();

        // then
        assertThat(findSections)
                .extracting(SectionEntity::getDistance)
                .containsExactlyElementsOf(sections.stream().map(SectionEntity::getDistance).collect(Collectors.toList()));
    }

    @Test
    void ID를_기준으로_구간을_조회한다() {
        // given
        LineEntity saveLine1 = lineDao.insert(LINE_NO_1);
        final Long lineId1 = saveLine1.getId();
        StationEntity station1 = stationDao.insert(YONGSAN_STATION);
        StationEntity station2 = stationDao.insert(NAMYOUNG_STATION);
        StationEntity station3 = stationDao.insert(SEOUL_STATION);
        final SectionEntity section1 = new SectionEntity(station1.getId(), station2.getId(), lineId1, 3);
        final SectionEntity section2 = new SectionEntity(station2.getId(), station3.getId(), lineId1, 4);
        sectionDao.insertAll(List.of(section1, section2));

        LineEntity saveLine2 = lineDao.insert(LINE_NO_2);
        final Long lineId2 = saveLine2.getId();
        StationEntity station4 = stationDao.insert(GANGNAM_STATION);
        StationEntity station5 = stationDao.insert(YEOKSAM_STATION);
        StationEntity station6 = stationDao.insert(SEOLLEUNG_STATION);
        StationEntity station7 = stationDao.insert(SAMSEONG_STATION);
        final SectionEntity section3 = new SectionEntity(station4.getId(), station5.getId(), lineId2, 5);
        final SectionEntity section4 = new SectionEntity(station5.getId(), station6.getId(), lineId2, 6);
        final SectionEntity section5 = new SectionEntity(station6.getId(), station7.getId(), lineId2, 7);
        final List<SectionEntity> sections = List.of(section3, section4, section5);
        sectionDao.insertAll(sections);

        // when
        final List<SectionEntity> findSections = sectionDao.findByLineId(lineId2);

        // then
        assertThat(findSections)
                .extracting(SectionEntity::getDistance)
                .containsExactlyElementsOf(
                        sections.stream()
                                .map(SectionEntity::getDistance)
                                .collect(Collectors.toList())
                );
    }

    @Test
    void 역_기준으로_구간을_조회한다() {
        // given
        LineEntity saveLine = lineDao.insert(LINE_NO_2);
        final Long lineId = saveLine.getId();
        StationEntity station1 = stationDao.insert(GANGNAM_STATION);
        StationEntity station2 = stationDao.insert(YEOKSAM_STATION);
        StationEntity station3 = stationDao.insert(SEOLLEUNG_STATION);
        StationEntity station4 = stationDao.insert(SAMSEONG_STATION);
        final SectionEntity section1 = new SectionEntity(station1.getId(), station2.getId(), lineId, 5);
        final SectionEntity section2 = new SectionEntity(station2.getId(), station3.getId(), lineId, 6);
        final SectionEntity section3 = new SectionEntity(station3.getId(), station4.getId(), lineId, 7);
        final List<SectionEntity> sections = List.of(section1, section2, section3);
        sectionDao.insertAll(sections);

        // when
        final List<SectionEntity> findSections = sectionDao.findByStationId(station2.getId());

        // then
        assertThat(findSections)
                .allMatch(section -> section.getSourceStationId().equals(station2.getId())
                        || section.getTargetStationId().equals(station2.getId()));
    }

    @Test
    void 노선의_모든_구간을_삭제한다() {
        // given
        LineEntity saveLine = lineDao.insert(LINE_NO_2);
        final Long lineId = saveLine.getId();
        StationEntity station1 = stationDao.insert(GANGNAM_STATION);
        StationEntity station2 = stationDao.insert(YEOKSAM_STATION);
        StationEntity station3 = stationDao.insert(SEOLLEUNG_STATION);
        StationEntity station4 = stationDao.insert(SAMSEONG_STATION);
        final SectionEntity section1 = new SectionEntity(station1.getId(), station2.getId(), lineId, 5);
        final SectionEntity section2 = new SectionEntity(station2.getId(), station3.getId(), lineId, 6);
        final SectionEntity section3 = new SectionEntity(station3.getId(), station4.getId(), lineId, 7);
        final List<SectionEntity> sections = List.of(section1, section2, section3);
        sectionDao.insertAll(sections);

        // when
        sectionDao.deleteByLineId(lineId);

        // then
        assertThat(sectionDao.findByLineId(lineId)).isEmpty();
    }

    @Test
    void 노선의_특정_역이_존재하는_구간을_삭제한다() {
        // given
        LineEntity saveLine = lineDao.insert(LINE_NO_2);
        final Long lineId = saveLine.getId();
        StationEntity station1 = stationDao.insert(GANGNAM_STATION);
        StationEntity station2 = stationDao.insert(YEOKSAM_STATION);
        StationEntity station3 = stationDao.insert(SEOLLEUNG_STATION);
        StationEntity station4 = stationDao.insert(SAMSEONG_STATION);
        final SectionEntity section1 = new SectionEntity(station1.getId(), station2.getId(), lineId, 5);
        final SectionEntity section2 = new SectionEntity(station2.getId(), station3.getId(), lineId, 6);
        final SectionEntity section3 = new SectionEntity(station3.getId(), station4.getId(), lineId, 7);
        final List<SectionEntity> sections = List.of(section1, section2, section3);
        sectionDao.insertAll(sections);

        // when
        sectionDao.deleteByLineIdAndStationId(lineId, station2.getId());

        // then
        assertThat(sectionDao.findByStationId(station2.getId())).isEmpty();
    }


}
