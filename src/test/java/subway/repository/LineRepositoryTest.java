package subway.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;
import subway.fixture.LineFixture.이호선;
import subway.fixture.StationFixture.삼성역;
import subway.fixture.StationFixture.역삼역;
import subway.fixture.StationFixture.잠실역;

@SpringBootTest
class LineRepositoryTest {

    @Autowired
    private JdbcLineRepository lineRepository;
    @Autowired
    private LineDao lineDao;
    @Autowired
    private StationDao stationDao;
    @Autowired
    private SectionDao sectionDao;

    @Test
    void test() {
        LineEntity line = lineDao.insert(이호선.ENTITY);

        StationEntity station1 = stationDao.insert(잠실역.ENTITY);
        StationEntity station2 = stationDao.insert(삼성역.ENTITY);
        StationEntity station3 = stationDao.insert(역삼역.ENTITY);

        sectionDao.insert(new SectionEntity(line.getId(), station1.getId(), station2.getId(), 5));
        sectionDao.insert(new SectionEntity(line.getId(), station2.getId(), station3.getId(), 7));

        lineRepository.findById(line.getId());
    }
}
