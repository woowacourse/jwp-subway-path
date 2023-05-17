package subway.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.repository.SectionRepository;

@SuppressWarnings("NonAsciiCharacters")
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
    protected Long 구호선;
    protected Long 잠실역;
    protected Long 삼성역;
    protected Long 잠실새내역;
    protected Long 선릉역;
    protected Long 몽촌토성역;
    protected Long 석촌역;
    protected Long 올림픽공원역;

    @BeforeEach
    void init() {
        save();
    }

    /**
     *       8호선                    9호선
     *      몽촌토성                올림픽공원
     *         |                 /
     *        10               20
     *         |              /
     * 2호선 : 잠실 -- 10 -- 잠실새내 -- 20 -- 삼성 -- 15 -- 선릉
     *         |          /
     *        15       30
     *         |      /
     *         |    /
     *         석촌
     */
    private void save() {
        이호선 = lineDao.insert(new LineEntity("2호선", "초록색"));
        팔호선 = lineDao.insert(new LineEntity("8호선", "분홍색"));
        구호선 = lineDao.insert(new LineEntity("9호선", "갈색"));

        잠실역 = stationDao.insert(new StationEntity("잠실역")).getId();
        잠실새내역 = stationDao.insert(new StationEntity("잠실새내역")).getId();
        삼성역 = stationDao.insert(new StationEntity("삼성역")).getId();
        선릉역 = stationDao.insert(new StationEntity("선릉역")).getId();
        몽촌토성역 = stationDao.insert(new StationEntity("몽촌토성역")).getId();
        석촌역 = stationDao.insert(new StationEntity("석촌역")).getId();
        올림픽공원역 = stationDao.insert(new StationEntity("올림픽공원역")).getId();

        sectionDao.insert(new SectionEntity(10, 잠실역, 잠실새내역, 이호선));
        sectionDao.insert(new SectionEntity(20, 잠실새내역, 삼성역, 이호선));
        sectionDao.insert(new SectionEntity(20, 삼성역, 선릉역, 이호선));

        sectionDao.insert(new SectionEntity(15, 석촌역, 잠실역, 팔호선));
        sectionDao.insert(new SectionEntity(10, 잠실역, 몽촌토성역, 팔호선));

        sectionDao.insert(new SectionEntity(30, 석촌역, 잠실새내역, 구호선));
        sectionDao.insert(new SectionEntity(20, 잠실새내역, 올림픽공원역, 구호선));
    }
}
