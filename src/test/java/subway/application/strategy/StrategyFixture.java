package subway.application.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.SectionStationDao;
import subway.dao.StationDao;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.Station;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
@Sql("classpath:/remove-section-line.sql")
@Import({StationDao.class, SectionDao.class, LineDao.class, SectionStationDao.class})
public abstract class StrategyFixture {

    @Autowired
    protected StationDao stationDao;
    @Autowired
    protected SectionDao sectionDao;
    @Autowired
    protected LineDao lineDao;
    @Autowired
    protected SectionStationDao sectionStationDao;

    protected Long 이호선;
    protected Station 잠실역;
    protected Station 삼성역;
    protected Station 잠실새내역;
    protected Station 선릉역;

    @BeforeEach
    void setUp() {
        save();
    }

    /**
     * 이호선 : 잠실 -- 10 -- 잠실새내 -- 20 -- 삼성 -- 15 -- 선릉
     */
    private void save() {
        이호선 = lineDao.insert(new LineEntity("2호선", "초록색"));

        잠실역 = createStation("잠실역");
        잠실새내역 = createStation("잠실새내역");
        삼성역 = createStation("삼성역");
        선릉역 = createStation("선릉역");

        sectionDao.insert(new SectionEntity(10, 잠실역.getId(), 잠실새내역.getId(), 이호선));
        sectionDao.insert(new SectionEntity(20, 잠실새내역.getId(), 삼성역.getId(), 이호선));
        sectionDao.insert(new SectionEntity(15, 삼성역.getId(), 선릉역.getId(), 이호선));
    }

    protected Station createStation(String name) {
        final StationEntity entity = stationDao.insert(new StationEntity(name));
        return new Station(entity.getId(), entity.getName());
    }
}
