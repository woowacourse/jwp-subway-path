package subway.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
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

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class LineJdbcRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineDao lineDao;
    private StationDao stationDao;
    private SectionDao sectionDao;
    private LineJdbcRepository lineJdbcRepository;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate, jdbcTemplate.getDataSource());
        sectionDao = new SectionDao(jdbcTemplate, jdbcTemplate.getDataSource());
        stationDao = new StationDao(jdbcTemplate, jdbcTemplate.getDataSource());
        lineJdbcRepository = new LineJdbcRepository(jdbcTemplate, lineDao, sectionDao);
    }

    @Test
    void 아이디로_조회_테스트() {
        // given
        LineEntity line = lineDao.insert(new LineEntity("2호선", "GREEN", 0));

        StationEntity station1 = stationDao.insert(잠실역.ENTITY);
        StationEntity station2 = stationDao.insert(삼성역.ENTITY);
        StationEntity station3 = stationDao.insert(역삼역.ENTITY);

        SectionEntity sectionEntity1 = sectionDao.insert(
                new SectionEntity(line.getId(), station1.getId(), station2.getId(), 5));
        SectionEntity sectionEntity2 = sectionDao.insert(
                new SectionEntity(line.getId(), station2.getId(), station3.getId(), 7));

        // when
        Line result = lineJdbcRepository.findById(line.getId()).get();

        // then
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .ignoringCollectionOrder()
                .isEqualTo(new Line(line.getId(), "2호선", "GREEN", 0,
                        List.of(
                                new Section(sectionEntity1.getId(), new Station(station1.getId(), "잠실역"),
                                        new Station(station2.getId(), "삼성역"), 5),
                                new Section(sectionEntity2.getId(), new Station(station2.getId(), "삼성역"),
                                        new Station(station3.getId(), "역삼역"), 7)
                        )));
    }

    @Test
    void 역을_포함하는_노선_아이디_리스트_조회() {
        // given
        long stationId = 1L;
        Station station = new Station(stationId, "잠실역");
        List<Long> expectedLineIds = List.of(1L, 2L, 3L);
        List<Long> unExpectedLindIds = List.of(4L, 5L);

        for (final Long lineId : expectedLineIds) {
            sectionDao.insert(new SectionEntity(lineId, stationId, 2L, 5));
        }

        for (final Long lineId : unExpectedLindIds) {
            sectionDao.insert(new SectionEntity(lineId, 2L, 3L, 5));
        }

        // when
        List<Long> actual = lineJdbcRepository.findContainingLineIdsByStation(station);

        // then
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringCollectionOrder()
                .isEqualTo(expectedLineIds);
    }

    @Test
    void 구간_갱신_테스트() {
        // given
        LineEntity line = lineDao.insert(new LineEntity("2호선", "GREEN", 0));

        StationEntity station1 = stationDao.insert(잠실역.ENTITY);
        StationEntity station2 = stationDao.insert(삼성역.ENTITY);
        StationEntity station3 = stationDao.insert(역삼역.ENTITY);

        sectionDao.insert(
                new SectionEntity(line.getId(), station1.getId(), station2.getId(), 5));
        sectionDao.insert(
                new SectionEntity(line.getId(), station2.getId(), station3.getId(), 7));

        // when
        lineJdbcRepository.updateSections(new Line(line.getId(), line.getName(), line.getColor(), line.getSurcharge(),
                List.of(new Section(new Station(station1.getId(), station1.getName()),
                        new Station(station3.getId(), station3.getName()), 12))));

        // then
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
            lineDao.insert(new LineEntity("2호선", "GREEN", 0));

            // when
            boolean result = lineJdbcRepository.checkExistByName("2호선");

            // then
            assertThat(result).isTrue();
        }

        @Test
        void 없으면_거짓() {
            // when
            boolean result = lineJdbcRepository.checkExistByName("2호선");

            // then
            assertThat(result).isFalse();
        }
    }
}
