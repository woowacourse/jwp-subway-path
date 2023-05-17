package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
public class SectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SectionDao sectionDao;
    private LineDao lineDao;
    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate);
        lineDao = new LineDao(jdbcTemplate);
        stationDao = new StationDao(jdbcTemplate);
    }

    @Test
    void 구간을_전체_저장한다() {
        // given
        final LineEntity line = lineDao.insert(new LineEntity("1호선", "RED", 0));
        final StationEntity station1 = stationDao.insert(new StationEntity("A", line.getId()));
        final StationEntity station2 = stationDao.insert(new StationEntity("B", line.getId()));
        final StationEntity station3 = stationDao.insert(new StationEntity("C", line.getId()));

        final List<SectionEntity> sections = List.of(
                new SectionEntity(station1.getId(), station2.getId(), 3, line.getId()),
                new SectionEntity(station2.getId(), station3.getId(), 5, line.getId())
        );

        // when
        sectionDao.insertAll(sections);

        // then
        final List<SectionEntity> result = sectionDao.findAll();
        assertThat(result).usingRecursiveComparison().ignoringFields("id").isEqualTo(sections);
    }

    @Test
    void 구간을_전체_조회한다() {
        // given
        final LineEntity line = lineDao.insert(new LineEntity("1호선", "RED", 0));
        final StationEntity station1 = stationDao.insert(new StationEntity("A", line.getId()));
        final StationEntity station2 = stationDao.insert(new StationEntity("B", line.getId()));
        final StationEntity station3 = stationDao.insert(new StationEntity("C", line.getId()));

        final List<SectionEntity> sections = List.of(
                new SectionEntity(station1.getId(), station2.getId(), 3, line.getId()),
                new SectionEntity(station2.getId(), station3.getId(), 5, line.getId())
        );
        sectionDao.insertAll(sections);

        // when
        final List<SectionEntity> result = sectionDao.findAll();

        // then
        assertThat(result).usingRecursiveComparison().ignoringFields("id").isEqualTo(sections);
    }

    @Test
    void 입력받은_노선_id에_해당하는_구간을_전체_삭제한다() {
        // given
        final LineEntity line = lineDao.insert(new LineEntity("1호선", "RED", 0));
        final StationEntity station1 = stationDao.insert(new StationEntity("A", line.getId()));
        final StationEntity station2 = stationDao.insert(new StationEntity("B", line.getId()));
        final StationEntity station3 = stationDao.insert(new StationEntity("C", line.getId()));

        final List<SectionEntity> sections = List.of(
                new SectionEntity(station1.getId(), station2.getId(), 3, line.getId()),
                new SectionEntity(station2.getId(), station3.getId(), 5, line.getId())
        );
        sectionDao.insertAll(sections);

        // when
        sectionDao.deleteAllByLineId(line.getId());

        // then
        assertThat(sectionDao.findAll()).hasSize(0);
    }

    @Test
    void 라인_id를_받아_구간을_조회한다() {
        // given
        final LineEntity line = lineDao.insert(new LineEntity("1호선", "RED", 0));
        final StationEntity station1 = stationDao.insert(new StationEntity("A", line.getId()));
        final StationEntity station2 = stationDao.insert(new StationEntity("B", line.getId()));
        final StationEntity station3 = stationDao.insert(new StationEntity("C", line.getId()));

        final List<SectionEntity> sections = List.of(
                new SectionEntity(station1.getId(), station2.getId(), 3, line.getId()),
                new SectionEntity(station2.getId(), station3.getId(), 5, line.getId())
        );
        sectionDao.insertAll(sections);

        // when
        final List<SectionEntity> result = sectionDao.findByLineId(line.getId());

        // then
        assertThat(result).usingRecursiveComparison().ignoringFields("id").isEqualTo(sections);
    }

    @Test
    void 구간_id를_받아_제거한다() {
        // given
        final LineEntity line = lineDao.insert(new LineEntity("1호선", "RED", 0));
        final StationEntity station1 = stationDao.insert(new StationEntity("A", line.getId()));
        final StationEntity station2 = stationDao.insert(new StationEntity("B", line.getId()));

        final List<SectionEntity> sections = List.of(
                new SectionEntity(station1.getId(), station2.getId(), 3, line.getId())
        );
        sectionDao.insertAll(sections);
        final SectionEntity sectionEntity = sectionDao.findByLineId(line.getId()).get(0);

        // when
        sectionDao.deleteById(sectionEntity.getId());

        // then
        final List<SectionEntity> result = sectionDao.findAll();
        assertThat(result).isEmpty();
    }
}
