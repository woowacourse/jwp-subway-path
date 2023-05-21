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
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

@JdbcTest
class SectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SectionDao sectionDao;
    private StationDao stationDao;
    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate);
        stationDao = new StationDao(jdbcTemplate);
        lineDao = new LineDao(jdbcTemplate);
    }

    @Test
    @DisplayName("해당 노선의 모든 구간 정보를 조회한다.")
    void findAllByLineId() {
        //given
        final LineEntity lineEntity = lineDao.save(new LineEntity("2호선", "초록색", 300));
        final StationEntity upward = stationDao.save(new StationEntity("잠실역"));
        final StationEntity downward = stationDao.save(new StationEntity("잠실새내역"));
        final SectionEntity entity = new SectionEntity(lineEntity.getId(), upward.getId(), downward.getId(), 10);
        final SectionEntity savedEntity = sectionDao.save(entity);

        //when
        final List<SectionEntity> sections = sectionDao.findAllByLineId(savedEntity.getLineId());

        //then
        assertThat(sections).containsExactly(savedEntity);
    }

    @Test
    @DisplayName("모든 구간 정보를 저장한다.")
    void saveAll() {
        //given
        final LineEntity lineEntity = lineDao.save(new LineEntity("2호선", "초록색", 300));
        final StationEntity upward = stationDao.save(new StationEntity("잠실역"));
        final StationEntity middle = stationDao.save(new StationEntity("잠실새내역"));
        final StationEntity downward = stationDao.save(new StationEntity("종합운동장역"));
        final List<SectionEntity> sections = List.of(
                new SectionEntity(lineEntity.getId(), upward.getId(), middle.getId(), 10),
                new SectionEntity(lineEntity.getId(), middle.getId(), downward.getId(), 10)
        );

        //when
        sectionDao.saveAll(sections);

        //then
        final List<SectionEntity> result = sectionDao.findAllByLineId(lineEntity.getId());
        assertAll(
                () -> assertThat(result).hasSize(2),
                () -> assertThat(result.get(0).getLineId()).isEqualTo(lineEntity.getId()),
                () -> assertThat(result.get(0).getUpwardStationId()).isEqualTo(upward.getId()),
                () -> assertThat(result.get(0).getUpwardStation()).isEqualTo(upward.getName()),
                () -> assertThat(result.get(0).getDownwardStationId()).isEqualTo(middle.getId()),
                () -> assertThat(result.get(0).getDownwardStation()).isEqualTo(middle.getName()),
                () -> assertThat(result.get(0).getDistance()).isEqualTo(10),
                () -> assertThat(result.get(1).getLineId()).isEqualTo(lineEntity.getId()),
                () -> assertThat(result.get(1).getUpwardStationId()).isEqualTo(middle.getId()),
                () -> assertThat(result.get(1).getUpwardStation()).isEqualTo(middle.getName()),
                () -> assertThat(result.get(1).getDownwardStationId()).isEqualTo(downward.getId()),
                () -> assertThat(result.get(1).getDownwardStation()).isEqualTo(downward.getName()),
                () -> assertThat(result.get(1).getDistance()).isEqualTo(10)
        );
    }

    @Test
    @DisplayName("노선의 구간 정보를 삭제한다.")
    void deleteAllByLineId() {
        //given
        final LineEntity lineEntity = lineDao.save(new LineEntity("2호선", "초록색", 300));
        final StationEntity upward = stationDao.save(new StationEntity("잠실역"));
        final StationEntity downward = stationDao.save(new StationEntity("잠실새내역"));
        final SectionEntity entity = new SectionEntity(lineEntity.getId(), upward.getId(), downward.getId(), 10);
        final SectionEntity savedEntity = sectionDao.save(entity);

        //when
        sectionDao.deleteAllByLineId(savedEntity.getLineId());

        //then
        final List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(savedEntity.getLineId());
        assertThat(sectionEntities).isEmpty();
    }
}
