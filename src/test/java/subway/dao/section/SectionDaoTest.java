package subway.dao.section;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.line.LineDao;
import subway.dao.line.LineEntity;
import subway.dao.station.StationDao;
import subway.dao.station.StationEntity;


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
        sectionDao = new SectionDao(jdbcTemplate);
        lineDao = new LineDao(jdbcTemplate);
        stationDao = new StationDao(jdbcTemplate);
    }

    @Test
    void 구간_저장_테스트() {
        //given
        final LineEntity insertLine = lineDao.insert(new LineEntity("2호선", "초록"));
        final Long firstStationId = stationDao.insert(new StationEntity("강남")).getStationId();
        final Long secondStationId = stationDao.insert(new StationEntity("역삼")).getStationId();
        final SectionEntity sectionEntity = new SectionEntity(
                firstStationId, secondStationId, 3, insertLine.getLineId()
        );

        //when
        final SectionEntity insertSection = sectionDao.insert(sectionEntity);

        //then
        assertThat(insertSection.getFirstStationId()).isEqualTo(firstStationId);
    }


    @Test
    void 모든_구간을_조회하는_테스트() {
        //given
        final LineEntity insertLine = lineDao.insert(new LineEntity("2호선", "초록"));
        final Long firstStationId = stationDao.insert(new StationEntity("강남")).getStationId();
        final Long secondStationId = stationDao.insert(new StationEntity("역삼")).getStationId();
        final SectionEntity sectionEntity = new SectionEntity(
                firstStationId, secondStationId, 3, insertLine.getLineId()
        );
        sectionDao.insert(sectionEntity);

        //when
        final List<SectionEntity> all = sectionDao.findAll();

        //then
        assertThat(all).hasSize(1);
    }

    @Test
    void 식별자로_구간_조회_테스트() {
        //given
        final LineEntity insertLine = lineDao.insert(new LineEntity("2호선", "초록"));
        final Long firstStationId = stationDao.insert(new StationEntity("강남")).getStationId();
        final Long secondStationId = stationDao.insert(new StationEntity("역삼")).getStationId();
        final SectionEntity sectionEntity = new SectionEntity(
                firstStationId, secondStationId, 3, insertLine.getLineId()
        );
        final SectionEntity insert = sectionDao.insert(sectionEntity);

        //when
        final SectionEntity byId = sectionDao.findById(insert.getId());

        //then
        assertThat(byId).isNotNull();
        assertThat(byId.getFirstStationId()).isEqualTo(firstStationId);
    }

    @Test
    void 노선_식별자로_구간_조회_테스트() {
        //given
        final LineEntity insertLine = lineDao.insert(new LineEntity("2호선", "초록"));
        final Long lineId = insertLine.getLineId();
        final Long firstStationId = stationDao.insert(new StationEntity("강남")).getStationId();
        final Long secondStationId = stationDao.insert(new StationEntity("역삼")).getStationId();
        final Long thirdStationId = stationDao.insert(new StationEntity("선릉")).getStationId();
        sectionDao.insert(new SectionEntity(firstStationId, secondStationId, 3, lineId));
        sectionDao.insert(new SectionEntity(secondStationId, thirdStationId, 2, lineId));

        //when
        final List<SectionEntity> byLineId = sectionDao.findByLineId(lineId);

        //then
        assertThat(byLineId).hasSize(2);
        assertThat(byLineId).extracting("lineId")
                .containsOnly(lineId);
    }

    @Test
    void 구간_삭제_테스트() {
        //given
        final LineEntity insertLine = lineDao.insert(new LineEntity("2호선", "초록"));
        final Long firstStationId = stationDao.insert(new StationEntity("강남")).getStationId();
        final Long secondStationId = stationDao.insert(new StationEntity("역삼")).getStationId();
        final SectionEntity sectionEntity = new SectionEntity(
                firstStationId, secondStationId, 3, insertLine.getLineId()
        );
        sectionDao.insert(sectionEntity);

        //when
        sectionDao.deleteByStations(firstStationId, secondStationId);

        //then
        final List<SectionEntity> all = sectionDao.findAll();
        assertThat(all).isEmpty();
    }
}
