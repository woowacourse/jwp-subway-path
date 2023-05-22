package subway.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.*;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.fixture.StationFixture.삼성역;
import subway.fixture.StationFixture.역삼역;
import subway.fixture.StationFixture.잠실역;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class LineRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineDao lineDao;
    private StationDao stationDao;
    private SectionDao sectionDao;
    private LineRepository lineRepository;

    @BeforeEach
    void setUp() {
        lineDao = new H2LineDao(jdbcTemplate, jdbcTemplate.getDataSource());
        sectionDao = new H2SectionDao(jdbcTemplate, jdbcTemplate.getDataSource());
        stationDao = new H2StationDao(jdbcTemplate, jdbcTemplate.getDataSource());
        lineRepository = new JdbcLineRepository(jdbcTemplate, lineDao, sectionDao);
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

        Line result = lineRepository.findById(line.getId());
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
    void 구간_갱신_테스트() {
        LineEntity line = lineDao.insert(new LineEntity("2호선", "GREEN"));

        StationEntity station1 = stationDao.insert(잠실역.ENTITY);
        StationEntity station2 = stationDao.insert(삼성역.ENTITY);
        StationEntity station3 = stationDao.insert(역삼역.ENTITY);

        sectionDao.insert(
                new SectionEntity(line.getId(), station1.getId(), station2.getId(), 5));
        sectionDao.insert(
                new SectionEntity(line.getId(), station2.getId(), station3.getId(), 7));

        lineRepository.updateSections(new Line(line.getId(), line.getName(), line.getColor(), line.getCost(),
                List.of(new Section(new Station(station1.getId(), station1.getName()),
                        new Station(station3.getId(), station3.getName()), 12))));

        List<SectionEntity> sections = sectionDao.findAllByLineId(line.getId());
        assertThat(sections)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .ignoringCollectionOrder()
                .isEqualTo(List.of(new SectionEntity(line.getId(), station1.getId(), station3.getId(), 12)));
    }
}
