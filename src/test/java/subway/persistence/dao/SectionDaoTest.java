package subway.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;

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
class SectionDaoTest {

    private final Line line = new Line("name", "color");
    private final Station upStation = new Station("upStation");
    private final Station downStation = new Station("downStation");
    private final long distance = 10L;
    @Autowired
    private LineDao lineDao;
    @Autowired
    private StationDao stationDao;
    @Autowired
    private SectionDao sectionDao;

    @Test
    @DisplayName("구간을 삽입한다.")
    void testInsert() {
        //given
        final Line savedLine = lineDao.insert(line);
        final Station savedUpStation = stationDao.insert(upStation);
        final Station savedDownStation = stationDao.insert(downStation);
        final Section section = new Section(savedUpStation, savedDownStation, distance);

        //when
        final Section savedSection = sectionDao.insert(section, savedLine.getId());

        //then
        assertThat(sectionDao.containId(savedSection.getId())).isTrue();
    }

    @Test
    @DisplayName("호선 id로 구간을 삭제한다.")
    void testDeleteAllByLineId() {
        //given
        final Line savedLine = lineDao.insert(line);
        final Station savedUpStation = stationDao.insert(upStation);
        final Station savedDownStation = stationDao.insert(downStation);
        final Section section = new Section(savedUpStation, savedDownStation, distance);
        final Section savedSection = sectionDao.insert(section, savedLine.getId());

        //when
        sectionDao.deleteAllByLineId(savedLine.getId());

        //then
        assertThat(sectionDao.containId(savedSection.getId())).isFalse();
    }
}
