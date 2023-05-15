package subway.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.SectionEntity;
import subway.domain.Line;
import subway.domain.Station;
import subway.repository.SectionRepository;

public abstract class SubwayFixture extends IntegrationTest {

    @Autowired
    protected StationDao stationDao;
    @Autowired
    protected SectionDao sectionDao;
    @Autowired
    protected LineDao lineDao;
    @Autowired
    protected SectionRepository sectionRepository;
    protected Long 이호선;
    protected Long 팔호선;
    protected Long 잠실역;
    protected Long 삼성역;
    protected Long 잠실새내역;
    protected Long 선릉역;
    protected Long 몽촌토성역;
    protected Long 석촌역;

    @BeforeEach
    void init() {
        save();
    }

    /**
     *       8호선
     *      몽촌토성
     *         |
     *        10
     *         |
     * 2호선 : 잠실 -- 10 -- 잠실새내 -- 20 -- 삼성 -- 15 -- 선릉
     *         |
     *        15
     *         |
     *        석촌
     */
    private void save() {
        이호선 = lineDao.insert(new Line("2호선", "초록색"));
        팔호선 = lineDao.insert(new Line("8호선", "분홍색"));

        잠실역 = stationDao.insert(new Station("잠실역")).getId();
        잠실새내역 = stationDao.insert(new Station("잠실새내역")).getId();
        삼성역 = stationDao.insert(new Station("삼성역")).getId();
        선릉역 = stationDao.insert(new Station("선릉역")).getId();
        몽촌토성역 = stationDao.insert(new Station("몽촌토성역")).getId();
        석촌역 = stationDao.insert(new Station("석촌역")).getId();

        sectionDao.insert(new SectionEntity(10, 잠실역, 잠실새내역, 이호선));
        sectionDao.insert(new SectionEntity(20, 잠실새내역, 삼성역, 이호선));
        sectionDao.insert(new SectionEntity(15, 삼성역, 선릉역, 이호선));

        sectionDao.insert(new SectionEntity(15, 석촌역, 잠실역, 팔호선));
        sectionDao.insert(new SectionEntity(10, 잠실역, 몽촌토성역, 팔호선));
    }
}
