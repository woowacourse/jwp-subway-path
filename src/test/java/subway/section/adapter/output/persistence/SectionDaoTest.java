package subway.section.adapter.output.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.line.adapter.output.persistence.LineDao;
import subway.line.adapter.output.persistence.LineEntity;
import subway.station.adapter.output.persistence.StationDao;
import subway.station.adapter.output.persistence.StationEntity;


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
        stationDao.insert(new StationEntity("강남"));
        stationDao.insert(new StationEntity("역삼"));
        final SectionEntity sectionEntity = new SectionEntity(
                "강남", "역삼", 3, insertLine.getLineId()
        );

        //when
        final SectionEntity insertSection = sectionDao.insert(sectionEntity);

        //then
        assertThat(insertSection.getFirstStation()).isEqualTo("강남");
    }


    @Test
    void 모든_구간을_조회하는_테스트() {
        //given
        final LineEntity insertLine = lineDao.insert(new LineEntity("2호선", "초록"));
        stationDao.insert(new StationEntity("강남"));
        stationDao.insert(new StationEntity("역삼"));
        final SectionEntity sectionEntity = new SectionEntity(
                "강남", "역삼", 3, insertLine.getLineId()
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
        stationDao.insert(new StationEntity("강남"));
        stationDao.insert(new StationEntity("역삼"));
        final SectionEntity sectionEntity = new SectionEntity(
                "강남", "역삼", 3, insertLine.getLineId()
        );
        final SectionEntity insert = sectionDao.insert(sectionEntity);

        //when
        final SectionEntity byId = sectionDao.findById(insert.getId());

        //then
        assertThat(byId).isNotNull();
        assertThat(byId.getFirstStation()).isEqualTo("강남");
    }

    @Test
    void 노선_식별자로_구간_조회_테스트() {
        //given
        final LineEntity insertLine = lineDao.insert(new LineEntity("2호선", "초록"));
        final Long lineId = insertLine.getLineId();
        stationDao.insert(new StationEntity("강남"));
        stationDao.insert(new StationEntity("역삼"));
        stationDao.insert(new StationEntity("선릉"));
        sectionDao.insert(new SectionEntity("강남", "역삼", 3, lineId));
        sectionDao.insert(new SectionEntity("역삼", "선릉", 2, lineId));

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
        stationDao.insert(new StationEntity("강남"));
        stationDao.insert(new StationEntity("역삼"));
        final SectionEntity sectionEntity = new SectionEntity(
                "강남", "역삼", 3, insertLine.getLineId()
        );
        sectionDao.insert(sectionEntity);

        //when
        sectionDao.deleteByStations("강남", "역삼");

        //then
        final List<SectionEntity> all = sectionDao.findAll();
        assertThat(all).isEmpty();
    }
}
