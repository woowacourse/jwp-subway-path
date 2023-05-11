package subway.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import subway.persistence.entity.LineEntity;
import subway.persistence.entity.SectionEntity;

@JdbcTest
class SectionDaoTest {

    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert simpleJdbcInsertForSection;
    private final SimpleJdbcInsert simpleJdbcInsertForLine;

    private SectionDao sectionDao;

    private final RowMapper<SectionEntity> rowMapper = (resultSet, rowNumber) -> new SectionEntity(
            resultSet.getLong("id"),
            resultSet.getLong("line_id"),
            resultSet.getString("upward_station"),
            resultSet.getString("downward_station"),
            resultSet.getInt("distance")
    );

    @Autowired
    SectionDaoTest(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.sectionDao = new SectionDao(namedParameterJdbcTemplate);
        this.simpleJdbcInsertForSection = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName("section")
                .usingColumns("line_id", "upward_station", "downward_station", "distance")
                .usingGeneratedKeyColumns("id");
        this.simpleJdbcInsertForLine = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName("line")
                .usingColumns("name", "upward_terminus", "downward_terminus")
                .usingGeneratedKeyColumns("id");
    }

    @DisplayName("DB에 구간을 삽입한다.")
    @Test
    void shouldInsertSectionWhenRequest() {
        LineEntity lineEntity = new LineEntity("2호선", "잠실역", "몽촌토성역");
        SqlParameterSource params = new BeanPropertySqlParameterSource(lineEntity);
        Long lineId = simpleJdbcInsertForLine.executeAndReturnKey(params).longValue();

        SectionEntity sectionEntityToInsert = new SectionEntity(lineId, "잠실역", "몽촌토성역", 3);
        sectionDao.insert(sectionEntityToInsert);

        String sql = "SELECT id, line_id, upward_station, downward_station, distance FROM section WHERE line_id=:lineId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("lineId", lineId);
        SectionEntity actualSectionEntity = namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource,
                rowMapper);

        assertAll(
                () -> assertThat(actualSectionEntity.getUpwardStation())
                        .isEqualTo(sectionEntityToInsert.getUpwardStation()),
                () -> assertThat(actualSectionEntity.getDownwardStation())
                        .isEqualTo(sectionEntityToInsert.getDownwardStation())
        );
    }

    @DisplayName("DB에서 특정 노선에 속하는 모든 구간을 가져온다.")
    @Test
    void shouldReadAllSectionsWhenRequestLineId() {
        LineEntity lineEntity = new LineEntity("2호선", "잠실역", "몽촌토성역");
        SqlParameterSource params = new BeanPropertySqlParameterSource(lineEntity);
        Long lineId = simpleJdbcInsertForLine.executeAndReturnKey(params).longValue();

        SectionEntity sectionEntityToInsert1 = new SectionEntity(lineId, "잠실역", "몽촌토성역", 3);
        sectionDao.insert(sectionEntityToInsert1);

        SectionEntity sectionEntityToInsert2 = new SectionEntity(lineId, "몽촌토성역", "까치산역", 3);
        sectionDao.insert(sectionEntityToInsert2);

        List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(lineId);
        assertAll(
                () -> assertThat(sectionEntities).hasSize(2),
                () -> assertThat(sectionEntities.get(0).getUpwardStation())
                        .isEqualTo(sectionEntityToInsert1.getUpwardStation()),
                () -> assertThat(sectionEntities.get(1).getDownwardStation())
                        .isEqualTo(sectionEntityToInsert2.getDownwardStation())
        );
    }

    @DisplayName("DB에서 특정 노선에 속하는 모든 구간을 삭제한다.")
    @Test
    void shouldDeleteSectionsWhenRequestLineId() {
        LineEntity lineEntity = new LineEntity("2호선", "잠실역", "몽촌토성역");
        SqlParameterSource params = new BeanPropertySqlParameterSource(lineEntity);
        Long lineId = simpleJdbcInsertForLine.executeAndReturnKey(params).longValue();

        SectionEntity sectionEntityToInsert1 = new SectionEntity(lineId, "잠실역", "몽촌토성역", 3);
        sectionDao.insert(sectionEntityToInsert1);

        SectionEntity sectionEntityToInsert2 = new SectionEntity(lineId, "몽촌토성역", "까치산역", 3);
        sectionDao.insert(sectionEntityToInsert2);

        sectionDao.deleteAllByLineId(lineId);
        String sql = "SELECT id, line_id, upward_station, downward_station, distance FROM section WHERE line_id=:lineId";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("lineId", lineId);
        List<SectionEntity> actualSectionEntities = namedParameterJdbcTemplate.query(sql, sqlParameterSource,
                rowMapper);
        assertThat(actualSectionEntities).isEmpty();
    }
}
