package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
import subway.entity.Section;
import subway.fixture.LineFixture.이호선;
import subway.fixture.SectionFixture;
import subway.fixture.SectionFixture.이호선_삼성_잠실;
import subway.fixture.SectionFixture.이호선_역삼_삼성;
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

    private RowMapper<Section> sectionRowMapper = (rs, rowNum) ->
            new Section(
                    rs.getLong("id"),
                    rs.getLong("line_id"),
                    rs.getLong("origin_id"),
                    rs.getLong("destination_id"),
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
        Section section = sectionDao.insert(SectionFixture.이호선_역삼_삼성.SECTION);
        Section result = jdbcTemplate.queryForObject("SELECT * FROM section WHERE id = ?", sectionRowMapper,
                section.getId());
        assertThat(result).isNotNull();
    }

    @Test
    void 전체_조회_테스트() {
        jdbcTemplate.update("INSERT INTO section(line_id, origin_id, destination_id, distance) VALUES (?,?,?,?)",
                이호선.LINE.getId(), 역삼역.STATION.getId(), 삼성역.STATION.getId(), 3);
        jdbcTemplate.update("INSERT INTO section(line_id, origin_id, destination_id, distance) VALUES (?,?,?,?)",
                이호선.LINE.getId(), 삼성역.STATION.getId(), 잠실역.STATION.getId(), 2);
        List<Section> sections = sectionDao.findAll();
        assertAll(
                () -> assertThat(sections.size())
                        .isEqualTo(2),
                () -> assertThat(sections)
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(List.of(이호선_역삼_삼성.SECTION, 이호선_삼성_잠실.SECTION))
        );
    }

    @Test
    void 아이디로_조회_테스트() {
        Map<String, Object> params = new HashMap<>();
        params.put("line_id", 이호선.LINE.getId());
        params.put("origin_id", 삼성역.STATION.getId());
        params.put("destination_id", 잠실역.STATION.getId());
        params.put("distance", 2);

        Long sectionId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        Section section = sectionDao.findById(sectionId);

        assertThat(section)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(이호선_삼성_잠실.SECTION);
    }

    @Test
    void 갱신_테스트() {
        Map<String, Object> params = new HashMap<>();
        params.put("line_id", 이호선.LINE.getId());
        params.put("origin_id", 삼성역.STATION.getId());
        params.put("destination_id", 잠실역.STATION.getId());
        params.put("distance", 2);

        Long sectionId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        sectionDao.update(new Section(sectionId, 이호선.LINE.getId(), 삼성역.STATION.getId(), 잠실역.STATION.getId(), 5));

        Section result = jdbcTemplate.queryForObject("SELECT * FROM section WHERE id = ?", sectionRowMapper, sectionId);
        assertThat(result.getDistance()).isEqualTo(5);
    }

    @Test
    void 삭제_테스트() {
        Map<String, Object> params = new HashMap<>();
        params.put("line_id", 이호선.LINE.getId());
        params.put("origin_id", 삼성역.STATION.getId());
        params.put("destination_id", 잠실역.STATION.getId());
        params.put("distance", 2);

        Long sectionId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        sectionDao.deleteById(sectionId);

        List<Section> result = jdbcTemplate.query("SELECT * FROM section WHERE id = ?", sectionRowMapper, sectionId);
        assertThat(result).isEmpty();
    }
}
