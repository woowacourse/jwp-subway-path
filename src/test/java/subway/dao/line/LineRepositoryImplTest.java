package subway.dao.line;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import subway.dao.section.SectionDao;
import subway.dao.section.SectionEntity;
import subway.dao.station.StationDao;
import subway.dao.station.StationEntity;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.domain.section.Sections;


@SuppressWarnings("NonAsciiCharacters")
@Import({LineDao.class, SectionDao.class, StationDao.class, LineRepositoryImpl.class})
@JdbcTest
class LineRepositoryImplTest {

    @Autowired
    private LineDao lineDao;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private StationDao stationDao;

    @Autowired
    private LineRepository lineRepository;

    @Test
    void 노선_조회_테스트() {
        //given
        final Long lineId = lineDao.insert(new LineEntity("1호선", "파랑")).getLineId();
        final Long firstStationId = stationDao.insert(new StationEntity("강남")).getStationId();
        final Long secondStationId = stationDao.insert(new StationEntity("역삼")).getStationId();
        sectionDao.insert(new SectionEntity(firstStationId, secondStationId, 3, lineId));

        //when
        final Line line = lineRepository.findById(lineId);

        //then
        assertThat(line.getLineName()).isEqualTo("1호선");
        final Sections sections = line.getSections();
        assertThat(sections.getSections()).hasSize(1);
    }

}
