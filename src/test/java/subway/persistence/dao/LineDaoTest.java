package subway.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

@JdbcTest(includeFilters = {
    @Filter(type = FilterType.ANNOTATION, value = Repository.class)
})
class LineDaoTest {

    private final Station topStation = new Station(1L, "topStation");
    private final Station midUpStation = new Station(2L, "midUpStation");
    private final Station midDownStation = new Station(3L, "midDownStation");
    private final Station bottomStation = new Station(4L, "bottomStation");
    private final long distance = 10L;
    private final Line line = new Line("lineName", "lineColor");
    @Autowired
    private LineDao lineDao;
    @Autowired
    private StationDao stationDao;
    @Autowired
    private SectionDao sectionDao;

    @Test
    @DisplayName("라인을 저장한다.")
    void testInsert() {
        //given
        //when
        final Line insertedLine = lineDao.insert(line);

        //then
        assertThat(insertedLine).isNotNull();
    }

    @Test
    @DisplayName("이름으로 Line을 조회한다.")
    void testFindByName() {
        //given
        final Line insertedLine = lineDao.insert(line);
        final Station insertedTopStation = stationDao.insert(topStation);
        final Station insertedMidUpStation = stationDao.insert(midUpStation);
        final Station insertedMidDownStation = stationDao.insert(midDownStation);
        final Station insertedBottomStation = stationDao.insert(bottomStation);
        final Section topSection = new Section(insertedTopStation, insertedMidUpStation, distance);
        final Section midSection = new Section(insertedMidUpStation, insertedMidDownStation, distance);
        final Section bottomSection = new Section(insertedMidDownStation, insertedBottomStation, distance);
        sectionDao.insert(topSection, insertedLine.getId());
        sectionDao.insert(midSection, insertedLine.getId());
        sectionDao.insert(bottomSection, insertedLine.getId());

        //when
        final List<LineSectionStationJoinDto> joinDtos = lineDao.findByName(insertedLine.getName());

        //then
        assertThat(joinDtos).hasSize(3);
    }
}
