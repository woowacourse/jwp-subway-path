package subway.adapter.out.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import subway.adapter.out.persistence.entity.SectionEntity;
import subway.fixture.LineFixture.이호선;
import subway.fixture.StationFixture.건대역;
import subway.fixture.StationFixture.삼성역;
import subway.fixture.StationFixture.역삼역;
import subway.fixture.StationFixture.잠실역;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class SectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert simpleJdbcInsert;

    private SectionDao sectionDao;

    private final RowMapper<SectionEntity> sectionRowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
                    rs.getLong("line_id"),
                    rs.getLong("up_station_id"),
                    rs.getLong("down_station_id"),
                    rs.getInt("distance")
            );

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate, jdbcTemplate.getDataSource());
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    @Test
    void 삽입_테스트() {
        // given
        SectionEntity entity = new SectionEntity(1L, 2L, 3L, 1);

        // when
        SectionEntity response = sectionDao.insert(entity);

        // then
        String sql = "SELECT * FROM section WHERE id = ?";
        SectionEntity result = jdbcTemplate.queryForObject(sql, sectionRowMapper,
                response.getId());
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(entity);
    }

    @Test
    void 여러개_삽입_테스트() {
        // given
        long lineId = -1L;
        List<SectionEntity> sectionEntities = List.of(
                new SectionEntity(lineId, 1L, 2L, 1),
                new SectionEntity(lineId, 3L, 4L, 2));

        // when
        sectionDao.insertAll(sectionEntities);

        // then
        String sql = "SELECT id, line_id, up_station_id, down_station_id, distance FROM section WHERE line_id = ?";
        List<SectionEntity> response = jdbcTemplate.query(sql, sectionRowMapper, lineId);
        assertThat(response)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .ignoringCollectionOrder()
                .isEqualTo(sectionEntities);
    }

    @Test
    void 전체_조회_테스트() {
        // given
        List<SectionEntity> entities = List.of(
                new SectionEntity(1L, 2L, 3L, 1),
                new SectionEntity(4L, 5L, 6L, 2)
        );

        for (final SectionEntity entity : entities) {
            jdbcTemplate.update(
                    "INSERT INTO section(line_id, up_station_id, down_station_id, distance) VALUES (?,?,?,?)",
                    entity.getLineId(), entity.getUpStationId(), entity.getDownStationId(), entity.getDistance());
        }

        // when
        List<SectionEntity> responses = sectionDao.findAll();

        // then
        assertAll(
                () -> assertThat(responses.size())
                        .isEqualTo(2),
                () -> assertThat(responses)
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .ignoringCollectionOrder()
                        .isEqualTo(entities)
        );
    }

    @Test
    void 라인_아이디로_조회_테스트() {
        // given
        long lineId = 1L;
        List<SectionEntity> targetEntities = List.of(
                new SectionEntity(lineId, 2L, 3L, 1),
                new SectionEntity(lineId, 5L, 6L, 2)
        );
        List<SectionEntity> otherEntities = List.of(
                new SectionEntity(lineId + 1L, 2L, 3L, 1),
                new SectionEntity(lineId + 2L, 5L, 6L, 2)
        );

        List<SectionEntity> entities = new ArrayList<>();
        entities.addAll(targetEntities);
        entities.addAll(otherEntities);

        for (final SectionEntity entity : entities) {
            jdbcTemplate.update(
                    "INSERT INTO section(line_id, up_station_id, down_station_id, distance) VALUES (?,?,?,?)",
                    entity.getLineId(), entity.getUpStationId(), entity.getDownStationId(), entity.getDistance());
        }

        // when
        List<SectionEntity> responses = sectionDao.findByLineId(lineId);

        // then
        assertThat(responses)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .ignoringCollectionOrder()
                .isEqualTo(targetEntities);
    }

    @Test
    void 역_아이디로_조회() {
        // given
        long stationId = 1L;
        List<SectionEntity> targetEntities = List.of(
                new SectionEntity(1L, stationId, 2L, 1),
                new SectionEntity(2L, 2L, stationId, 2)
        );
        List<SectionEntity> otherEntities = List.of(
                new SectionEntity(1L, 2L, 3L, 1),
                new SectionEntity(2L, 3L, 4L, 2)
        );

        List<SectionEntity> entities = new ArrayList<>();
        entities.addAll(targetEntities);
        entities.addAll(otherEntities);

        for (final SectionEntity entity : entities) {
            jdbcTemplate.update(
                    "INSERT INTO section(line_id, up_station_id, down_station_id, distance) VALUES (?,?,?,?)",
                    entity.getLineId(), entity.getUpStationId(), entity.getDownStationId(), entity.getDistance());
        }

        // when
        List<SectionEntity> responses = sectionDao.findByStationId(stationId);

        // then
        assertThat(responses)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .ignoringCollectionOrder()
                .isEqualTo(targetEntities);
    }

    @Test
    void 갱신_테스트() {
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("line_id", 이호선.ENTITY.getId());
        params.put("up_station_id", 삼성역.ENTITY.getId());
        params.put("down_station_id", 잠실역.ENTITY.getId());
        params.put("distance", 2);
        Long sectionId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        SectionEntity updateEntity = new SectionEntity(sectionId, 이호선.ENTITY.getId(), 역삼역.ENTITY.getId(),
                건대역.ENTITY.getId(), 5);

        // when
        sectionDao.update(updateEntity);

        // then
        SectionEntity result = jdbcTemplate.queryForObject("SELECT * FROM section WHERE id = ?", sectionRowMapper,
                sectionId);
        assertThat(result)
                .usingRecursiveComparison()
                .isEqualTo(updateEntity);
    }

    @Test
    void 삭제_테스트() {
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("line_id", 이호선.ENTITY.getId());
        params.put("up_station_id", 삼성역.ENTITY.getId());
        params.put("down_station_id", 잠실역.ENTITY.getId());
        params.put("distance", 2);
        Long sectionId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        // when
        sectionDao.deleteById(sectionId);

        // then
        List<SectionEntity> result = jdbcTemplate.query("SELECT * FROM section WHERE id = ?", sectionRowMapper,
                sectionId);
        assertThat(result).isEmpty();
    }

    @Test
    void 라인_아이디로_삭제_테스트() {
        // given
        Long lineId = 이호선.ENTITY.getId();

        Map<String, Object> params = new HashMap<>();
        params.put("line_id", lineId);
        params.put("up_station_id", 삼성역.ENTITY.getId());
        params.put("down_station_id", 잠실역.ENTITY.getId());
        params.put("distance", 2);

        simpleJdbcInsert.executeAndReturnKey(params).longValue();

        // when
        sectionDao.deleteAllByLineId(lineId);

        // then
        List<SectionEntity> result = jdbcTemplate.query("SELECT * FROM section WHERE line_id = ?", sectionRowMapper,
                lineId);
        assertThat(result).isEmpty();
    }
}
