package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

@JdbcTest
class SectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SectionDao sectionDao;
    private LineDao lineDao;
    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate);
        lineDao = new LineDao(jdbcTemplate, jdbcTemplate.getDataSource());
        stationDao = new StationDao(jdbcTemplate, jdbcTemplate.getDataSource());
    }

    @Test
    @DisplayName("Section 전체 삽입 테스트")
    void insertAll() {
        // given
        final Long lineId = lineDao.insert(LineEntity.from(new Line("2호선", "bg-green-600")));
        final Long stationId1 = stationDao.insert(StationEntity.from(new Station("용산역")));
        final Long stationId2 = stationDao.insert(StationEntity.from(new Station("죽전역")));
        final Long stationId3 = stationDao.insert(StationEntity.from(new Station("감삼역")));
        final SectionEntity sectionEntity1 = SectionEntity.of(lineId,
                new Section(new Station(stationId1, "용산역"), new Station(stationId2, "죽전역"), 10));
        final SectionEntity sectionEntity2 = SectionEntity.of(lineId,
                new Section(new Station(stationId2, "죽전역"), new Station(stationId3, "감삼역"), 5));
        // when
        sectionDao.insertAll(List.of(sectionEntity1, sectionEntity2));
        // then
        assertAll(
                () -> assertThat(sectionDao.findByLineId(lineId)).hasSize(2),
                () -> assertThat(sectionDao.findByLineId(lineId))
                        .usingRecursiveComparison()
                        .isEqualTo(List.of(sectionEntity1, sectionEntity2))
        );
    }

    @Test
    @DisplayName("lineId로 Section 조회 테스트")
    void findByLineId() {
        // given
        final Long lineId = lineDao.insert(LineEntity.from(new Line("2호선", "bg-green-600")));
        final Long stationId1 = stationDao.insert(StationEntity.from(new Station("용산역")));
        final Long stationId2 = stationDao.insert(StationEntity.from(new Station("죽전역")));
        final SectionEntity sectionEntity = SectionEntity.of(lineId,
                new Section(new Station(stationId1, "용산역"), new Station(stationId2, "죽전역"), 10));
        sectionDao.insertAll(List.of(sectionEntity));
        // when
        final List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineId);
        // then
        assertThat(sectionEntities)
                .usingRecursiveComparison()
                .isEqualTo(List.of(sectionEntity));
    }

    @Test
    @DisplayName("lineId로 Section 전체 삭제 테스트")
    void deleteAllByLineId() {
        // given
        final Long lineId = lineDao.insert(LineEntity.from(new Line("2호선", "bg-green-600")));
        final Long stationId1 = stationDao.insert(StationEntity.from(new Station("용산역")));
        final Long stationId2 = stationDao.insert(StationEntity.from(new Station("죽전역")));
        final SectionEntity sectionEntity = SectionEntity.of(lineId,
                new Section(new Station(stationId1, "용산역"), new Station(stationId2, "죽전역"), 10));
        sectionDao.insertAll(List.of(sectionEntity));
        // when
        sectionDao.deleteAllByLineId(lineId);
        // then
        assertThat(sectionDao.findByLineId(lineId)).isEmpty();
    }
}
