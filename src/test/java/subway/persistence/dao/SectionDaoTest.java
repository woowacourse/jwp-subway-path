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
import subway.domain.vo.Distance;
import subway.persistence.entity.LineEntity;
import subway.persistence.entity.SectionEntity;
import subway.persistence.entity.StationEntity;

@JdbcTest(includeFilters = {
    @Filter(type = FilterType.ANNOTATION, value = Repository.class)
})
class SectionDaoTest {

    private final Line line = new Line("name", "color", 0L);
    private final Station upStation = new Station("upStation");
    private final Station downStation = new Station("downStation");
    private final Distance distance = new Distance(10L);
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
        final LineEntity insertLineEntity = lineDao.insert(LineEntity.from(line));
        final StationEntity insertedUpStationEntity = stationDao.insert(StationEntity.from(upStation));
        final StationEntity insertedDownStationEntity = stationDao.insert(StationEntity.from(downStation));
        final Station upStation = new Station(insertedUpStationEntity.getId(), insertedUpStationEntity.getName());
        final Station downStation = new Station(insertedDownStationEntity.getId(), insertedDownStationEntity.getName());
        final Section section = new Section(upStation, downStation, distance);

        //when
        final SectionEntity insertedSection = sectionDao.insert(SectionEntity.from(section), insertLineEntity.getId());

        //then
        assertThat(sectionDao.containId(insertedSection.getId())).isTrue();
    }

    @Test
    @DisplayName("호선 id로 구간을 삭제한다.")
    void testDeleteAllByLineId() {
        //given
        final LineEntity insertLineEntity = lineDao.insert(LineEntity.from(line));
        final StationEntity insertedUpStationEntity = stationDao.insert(StationEntity.from(upStation));
        final StationEntity insertedDownStationEntity = stationDao.insert(StationEntity.from(downStation));
        final Station upStation = new Station(insertedUpStationEntity.getId(), insertedUpStationEntity.getName());
        final Station downStation = new Station(insertedDownStationEntity.getId(), insertedDownStationEntity.getName());
        final Section section = new Section(upStation, downStation, distance);
        final SectionEntity insertedSection = sectionDao.insert(SectionEntity.from(section), insertLineEntity.getId());

        //when
        sectionDao.deleteAllByLineId(insertLineEntity.getId());

        //then
        assertThat(sectionDao.containId(insertedSection.getId())).isFalse();
    }
}
