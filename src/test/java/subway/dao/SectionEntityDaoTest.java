package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import subway.entity.SectionEntity;
import subway.exception.NoSuchSectionException;
import subway.fixture.LineFixture.이호선;
import subway.fixture.SectionFixture.이호선_삼성_잠실_2;
import subway.fixture.SectionFixture.이호선_역삼_삼성_3;
import subway.fixture.StationFixture.삼성역;
import subway.fixture.StationFixture.역삼역;
import subway.fixture.StationFixture.잠실역;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class SectionEntityDaoTest {

    private final RowMapper<SectionEntity> sectionRowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
                    rs.getLong("line_id"),
                    rs.getLong("up_station_id"),
                    rs.getLong("down_station_id"),
                    rs.getInt("distance")
            );
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;
    private SectionDao sectionDao;

    @BeforeEach
    void setUp() {
        sectionDao = new H2SectionDao(jdbcTemplate, jdbcTemplate.getDataSource());
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    @Test
    void 삽입_테스트() {
        SectionEntity sectionEntity = sectionDao.insert(이호선_역삼_삼성_3.ENTITY);
        SectionEntity result = jdbcTemplate.queryForObject("SELECT * FROM section WHERE id = ?", sectionRowMapper,
                sectionEntity.getId());
        assertThat(result).isNotNull();
    }

    @Test
    void 전체_조회_테스트() {
        jdbcTemplate.update("INSERT INTO section(line_id, up_station_id, down_station_id, distance) VALUES (?,?,?,?)",
                이호선.ENTITY.getId(), 역삼역.ENTITY.getId(), 삼성역.ENTITY.getId(), 3);
        jdbcTemplate.update("INSERT INTO section(line_id, up_station_id, down_station_id, distance) VALUES (?,?,?,?)",
                이호선.ENTITY.getId(), 삼성역.ENTITY.getId(), 잠실역.ENTITY.getId(), 2);
        List<SectionEntity> sectionEntities = sectionDao.findAll();
        assertAll(
                () -> assertThat(sectionEntities)
                        .hasSize(2),
                () -> assertThat(sectionEntities)
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(List.of(이호선_역삼_삼성_3.ENTITY, 이호선_삼성_잠실_2.ENTITY))
        );
    }

    @Test
    void 아이디로_조회_테스트() {
        Map<String, Object> params = new HashMap<>();
        params.put("line_id", 이호선.ENTITY.getId());
        params.put("up_station_id", 삼성역.ENTITY.getId());
        params.put("down_station_id", 잠실역.ENTITY.getId());
        params.put("distance", 2);

        Long sectionId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        SectionEntity sectionEntity = sectionDao.findById(sectionId)
                .orElseThrow(() -> new NoSuchSectionException(sectionId));

        assertThat(sectionEntity)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(이호선_삼성_잠실_2.ENTITY);
    }

    @Test
    void 라인_아이디로_조회_테스트() {
        Long lineId = 이호선.ENTITY.getId();

        Map<String, Object> params = new HashMap<>();
        params.put("line_id", lineId);
        params.put("up_station_id", 삼성역.ENTITY.getId());
        params.put("down_station_id", 잠실역.ENTITY.getId());
        params.put("distance", 2);

        simpleJdbcInsert.executeAndReturnKey(params).longValue();

        List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(lineId);

        assertThat(sectionEntities)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(List.of(이호선_삼성_잠실_2.ENTITY));
    }

    @Test
    void 갱신_테스트() {
        Map<String, Object> params = new HashMap<>();
        params.put("line_id", 이호선.ENTITY.getId());
        params.put("up_station_id", 삼성역.ENTITY.getId());
        params.put("down_station_id", 잠실역.ENTITY.getId());
        params.put("distance", 2);

        Long sectionId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        sectionDao.update(new SectionEntity(sectionId, 이호선.ENTITY.getId(), 삼성역.ENTITY.getId(),
                잠실역.ENTITY.getId(), 5));

        SectionEntity result = jdbcTemplate.queryForObject("SELECT * FROM section WHERE id = ?", sectionRowMapper,
                sectionId);
        assertThat(result.getDistance()).isEqualTo(5);
    }

    @Test
    void 삭제_테스트() {
        Map<String, Object> params = new HashMap<>();
        params.put("line_id", 이호선.ENTITY.getId());
        params.put("up_station_id", 삼성역.ENTITY.getId());
        params.put("down_station_id", 잠실역.ENTITY.getId());
        params.put("distance", 2);

        Long sectionId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        sectionDao.deleteById(sectionId);

        List<SectionEntity> result = jdbcTemplate.query("SELECT * FROM section WHERE id = ?", sectionRowMapper,
                sectionId);
        assertThat(result).isEmpty();
    }

    @Test
    void 라인_아이디로_삭제_테스트() {
        Long lineId = 이호선.ENTITY.getId();

        Map<String, Object> params = new HashMap<>();
        params.put("line_id", lineId);
        params.put("up_station_id", 삼성역.ENTITY.getId());
        params.put("down_station_id", 잠실역.ENTITY.getId());
        params.put("distance", 2);

        simpleJdbcInsert.executeAndReturnKey(params).longValue();

        sectionDao.deleteAllByLineId(lineId);

        List<SectionEntity> result = jdbcTemplate.query("SELECT * FROM section WHERE line_id = ?", sectionRowMapper,
                lineId);
        assertThat(result).isEmpty();
    }
}
