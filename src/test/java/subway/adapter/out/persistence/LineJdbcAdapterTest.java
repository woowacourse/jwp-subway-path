package subway.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.adapter.out.persistence.dao.LineDao;
import subway.adapter.out.persistence.dao.SectionDao;
import subway.adapter.out.persistence.dao.StationDao;
import subway.adapter.out.persistence.entity.LineEntity;
import subway.adapter.out.persistence.entity.SectionEntity;
import subway.adapter.out.persistence.entity.StationEntity;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.fixture.StationFixture.삼성역;
import subway.fixture.StationFixture.역삼역;
import subway.fixture.StationFixture.잠실역;

@JdbcTest
class LineJdbcAdapterTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineDao lineDao;
    private StationDao stationDao;
    private SectionDao sectionDao;
    private LineJdbcAdapter lineJdbcAdapter;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate, jdbcTemplate.getDataSource());
        sectionDao = new SectionDao(jdbcTemplate, jdbcTemplate.getDataSource());
        stationDao = new StationDao(jdbcTemplate, jdbcTemplate.getDataSource());
        lineJdbcAdapter = new LineJdbcAdapter(jdbcTemplate, lineDao, sectionDao);
    }

    @Test
    void 아이디로_조회_테스트() {
        LineEntity line = lineDao.insert(new LineEntity("2호선", "GREEN"));

        StationEntity station1 = stationDao.insert(잠실역.ENTITY);
        StationEntity station2 = stationDao.insert(삼성역.ENTITY);
        StationEntity station3 = stationDao.insert(역삼역.ENTITY);

        SectionEntity sectionEntity1 = sectionDao.insert(
                new SectionEntity(line.getId(), station1.getId(), station2.getId(), 5));
        SectionEntity sectionEntity2 = sectionDao.insert(
                new SectionEntity(line.getId(), station2.getId(), station3.getId(), 7));

        Line result = lineJdbcAdapter.findById(line.getId()).get();
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .ignoringCollectionOrder()
                .isEqualTo(new Line(line.getId(), "2호선", "GREEN",
                        List.of(
                                new Section(sectionEntity1.getId(), new Station(station1.getId(), "잠실역"),
                                        new Station(station2.getId(), "삼성역"), 5),
                                new Section(sectionEntity2.getId(), new Station(station2.getId(), "삼성역"),
                                        new Station(station3.getId(), "역삼역"), 7)
                        )));
    }


    @Test
    void 구간_갱신_테스트() {
        LineEntity line = lineDao.insert(new LineEntity("2호선", "GREEN"));

        StationEntity station1 = stationDao.insert(잠실역.ENTITY);
        StationEntity station2 = stationDao.insert(삼성역.ENTITY);
        StationEntity station3 = stationDao.insert(역삼역.ENTITY);

        sectionDao.insert(
                new SectionEntity(line.getId(), station1.getId(), station2.getId(), 5));
        sectionDao.insert(
                new SectionEntity(line.getId(), station2.getId(), station3.getId(), 7));

        lineJdbcAdapter.updateSections(new Line(line.getId(), line.getName(), line.getColor(),
                List.of(new Section(new Station(station1.getId(), station1.getName()),
                        new Station(station3.getId(), station3.getName()), 12))));

        List<SectionEntity> sections = sectionDao.findByLineId(line.getId());
        assertThat(sections)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .ignoringCollectionOrder()
                .isEqualTo(List.of(new SectionEntity(line.getId(), station1.getId(), station3.getId(), 12)));
    }

    @Nested
    class 이름으로_노선_유무_조회시_ {

        @Test
        void 있으면_참() {
            // given
            lineDao.insert(new LineEntity("2호선", "GREEN"));

            // when
            boolean result = lineJdbcAdapter.checkExistByName("2호선");

            // then
            assertThat(result).isTrue();
        }

        @Test
        void 없으면_거짓() {
            // when
            boolean result = lineJdbcAdapter.checkExistByName("2호선");

            // then
            assertThat(result).isFalse();
        }
    }
}
