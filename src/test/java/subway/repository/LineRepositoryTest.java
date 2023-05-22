package subway.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

import java.util.Collections;

import static subway.fixture.StationFixture.강남1;
import static subway.fixture.StationFixture.잠실2;

@SpringBootTest
class LineRepositoryTest {

    private final LineRepository lineRepository;
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    @Autowired
    LineRepositoryTest(LineRepository lineRepository, LineDao lineDao, StationDao stationDao, SectionDao sectionDao) {
        this.lineRepository = lineRepository;
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Test
    void findById() {
        Line lineEntity = new Line("신림선", "노랑", Collections.emptyList());
        LineEntity line1 = lineDao.insert(lineEntity);

        System.out.println("라인" + line1.getId() + line1.getColor() + line1.getName());

        StationEntity stationEntity = new StationEntity("신림");
        StationEntity stationEntity1 = new StationEntity("당곡");
        lineRepository.saveStation(강남1);
        lineRepository.saveStation(잠실2);

        SectionEntity section = new SectionEntity(line1.getId(), 1, 2, 4);
        SectionEntity section1 = sectionDao.insert(section);

        System.out.println(section1.getDownStationId() + String.valueOf(section1.getUpStationId()));

        Line line = lineRepository.findById(line1.getId());
        System.out.println(line.getId());
        line.getSectionResponse().forEach(sectionResponse -> System.out.println("아아아아앙" + sectionResponse.getUpStationId() + sectionResponse.getDownStationId()));
    }
}