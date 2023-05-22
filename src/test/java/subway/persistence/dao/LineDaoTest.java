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
import subway.domain.vo.Distance;
import subway.persistence.entity.LineEntity;
import subway.persistence.entity.LineSectionStationJoinDto;
import subway.persistence.entity.SectionEntity;
import subway.persistence.entity.StationEntity;

@JdbcTest(includeFilters = {
    @Filter(type = FilterType.ANNOTATION, value = Repository.class)
})
class LineDaoTest {

    private final StationEntity topStationEntity = StationEntity.from(new Station(1L, "topStation"));
    private final StationEntity midUpStationEntity = StationEntity.from(new Station(2L, "midUpStation"));
    private final StationEntity midDownStationEntity = StationEntity.from(new Station(3L, "midDownStation"));
    private final StationEntity bottomStationEntity = StationEntity.from(new Station(4L, "bottomStation"));
    private final Distance distance = new Distance(10L);
    private final LineEntity lineEntity = LineEntity.from(new Line("lineName", "lineColor", 0L));
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
        final LineEntity insertedLineEntity = lineDao.insert(lineEntity);

        //then
        assertThat(insertedLineEntity).isNotNull();
    }

    @Test
    @DisplayName("이름으로 Line을 조회한다.")
    void testFindByName() {
        //given
        final LineEntity insertLineEntity = lineDao.insert(lineEntity);
        final StationEntity insertedTopStationEntity = stationDao.insert(topStationEntity);
        final StationEntity insertedMidUpStationEntity = stationDao.insert(midUpStationEntity);
        final StationEntity insertedMidDownStationEntity = stationDao.insert(midDownStationEntity);
        final StationEntity insertedBottomStationEntity = stationDao.insert(bottomStationEntity);
        final Station topStation = new Station(insertedTopStationEntity.getId(), insertedTopStationEntity.getName());
        final Station midUpStation = new Station(insertedMidUpStationEntity.getId(),
            insertedMidUpStationEntity.getName());
        final Station midDownStation = new Station(insertedMidDownStationEntity.getId(),
            insertedMidDownStationEntity.getName());
        final Station bottomStation = new Station(insertedBottomStationEntity.getId(),
            insertedBottomStationEntity.getName());
        final Section topSection = new Section(topStation, midUpStation, distance);
        final Section midSection = new Section(midUpStation, midDownStation, distance);
        final Section bottomSection = new Section(midDownStation, bottomStation, distance);
        sectionDao.insert(SectionEntity.from(topSection), insertLineEntity.getId());
        sectionDao.insert(SectionEntity.from(midSection), insertLineEntity.getId());
        sectionDao.insert(SectionEntity.from(bottomSection), insertLineEntity.getId());

        //when
        final List<LineSectionStationJoinDto> joinDtos = lineDao.findByName(insertLineEntity.getName());

        //then
        assertThat(joinDtos).hasSize(3);
    }
}
