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

@JdbcTest
class LineDaoTest {

    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert simpleJdbcInsert;

    private LineDao lineDao;

    private final RowMapper<LineEntity> rowMapper = (resultSet, rowNumber) -> new LineEntity(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("upward_terminus"),
            resultSet.getString("downward_terminus")
    );

    @Autowired
    LineDaoTest(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName("line")
                .usingColumns("name", "upward_terminus", "downward_terminus")
                .usingGeneratedKeyColumns("id");
        this.lineDao = new LineDao(namedParameterJdbcTemplate);
    }

    @DisplayName("DB에 노선을 삽입힌다.")
    @Test
    void shouldInsertLineWhenRequest() {
        LineEntity lineEntity = new LineEntity("2호선", "잠실역", "몽촌토성역");
        long id = lineDao.insert(lineEntity);

        String sql = "SELECT id, name, upward_terminus, downward_terminus FROM line WHERE id=:id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        LineEntity actualLineEntity = namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, rowMapper);

        assertAll(
                () -> assertThat(actualLineEntity.getUpwardTerminus())
                        .isEqualTo(lineEntity.getUpwardTerminus()),
                () -> assertThat(actualLineEntity.getDownwardTerminus())
                        .isEqualTo(lineEntity.getDownwardTerminus())
        );
    }

    @DisplayName("DB에서 ID로 특정 노선을 조회한다")
    @Test
    void shouldFindLineByIdFromDbWhenRequest() {
        LineEntity lineEntity = new LineEntity("2호선", "잠실역", "몽촌토성역");
        SqlParameterSource params = new BeanPropertySqlParameterSource(lineEntity);
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        LineEntity actualLineEntity = lineDao.findById(id);

        assertAll(
                () -> assertThat(actualLineEntity.getUpwardTerminus())
                        .isEqualTo(lineEntity.getUpwardTerminus()),
                () -> assertThat(actualLineEntity.getDownwardTerminus())
                        .isEqualTo(lineEntity.getDownwardTerminus())
        );
    }

    @DisplayName("DB에서 모든 노선을 조회한다")
    @Test
    void shouldFindAllLinesWhenRequest() {
        LineEntity lineEntity1 = new LineEntity("2호선", "잠실역", "몽촌토성역");
        SqlParameterSource params1 = new BeanPropertySqlParameterSource(lineEntity1);
        simpleJdbcInsert.executeAndReturnKey(params1).longValue();

        LineEntity lineEntity2 = new LineEntity("3호선", "수서역", "교대역");
        SqlParameterSource params2 = new BeanPropertySqlParameterSource(lineEntity2);
        simpleJdbcInsert.executeAndReturnKey(params2).longValue();

        List<LineEntity> actualLineEntities = lineDao.findAll();

        assertAll(
                () -> assertThat(actualLineEntities).hasSize(2),
                () -> assertThat(actualLineEntities.get(0).getUpwardTerminus())
                        .isEqualTo(lineEntity1.getUpwardTerminus()),
                () -> assertThat(actualLineEntities.get(0).getDownwardTerminus())
                        .isEqualTo(lineEntity1.getDownwardTerminus())
        );
    }

    @DisplayName("DB에 특정 노선을 업데이트한다.")
    @Test
    void shouldUpdateLineWhenRequest() {
        LineEntity lineEntity = new LineEntity("2호선", "잠실역", "몽촌토성역");
        SqlParameterSource params = new BeanPropertySqlParameterSource(lineEntity);
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        LineEntity lineEntityToUpdate = new LineEntity(id, "2호선", "강남역", "몽촌토성역");
        lineDao.update(lineEntityToUpdate);

        String sql = "SELECT id, name, upward_terminus, downward_terminus FROM line WHERE id=:id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        LineEntity actualLineEntity = namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, rowMapper);

        assertAll(
                () -> assertThat(actualLineEntity.getUpwardTerminus())
                        .isEqualTo(lineEntityToUpdate.getUpwardTerminus()),
                () -> assertThat(actualLineEntity.getDownwardTerminus())
                        .isEqualTo(lineEntity.getDownwardTerminus())
        );
    }
}
