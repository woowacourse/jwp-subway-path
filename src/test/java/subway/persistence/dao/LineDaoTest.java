package subway.persistence.dao;

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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
class LineDaoTest {

    @Autowired
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert simpleJdbcInsert;

    private LineDao lineDao;

    private final RowMapper<LineEntity> rowMapper = (resultSet, rowNumber) -> new LineEntity(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getLong("upward_terminus_id"),
            resultSet.getLong("downward_terminus_id"),
            resultSet.getInt("fare"));

    @Autowired
    LineDaoTest(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName("line")
                .usingColumns("name", "upward_terminus_id", "downward_terminus_id", "fare")
                .usingGeneratedKeyColumns("id");
        this.lineDao = new LineDao(namedParameterJdbcTemplate);
    }

    @DisplayName("DB에 노선을 삽입힌다.")
    @Test
    void shouldInsertLineWhenRequest() {
        LineEntity lineEntity = new LineEntity(
                1L,
                "2호선",
                1L,
                3L,
                0);
        long id = lineDao.insert(lineEntity);

        String sql = "SELECT id, name, upward_terminus_id, downward_terminus_id, fare FROM line WHERE id=:id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        LineEntity actualLineEntity = namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, rowMapper);

        assertAll(
                () -> assertThat(actualLineEntity.getUpwardTerminusId())
                        .isEqualTo(lineEntity.getUpwardTerminusId()),
                () -> assertThat(actualLineEntity.getDownwardTerminusId())
                        .isEqualTo(lineEntity.getDownwardTerminusId())
        );
    }

    @DisplayName("DB에서 ID로 특정 노선을 조회한다")
    @Test
    void shouldFindLineByIdFromDbWhenRequest() {
        LineEntity lineEntity = new LineEntity(
                1L,
                "2호선",
                1L,
                3L,
                0);
        SqlParameterSource params = new BeanPropertySqlParameterSource(lineEntity);
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        LineEntity actualLineEntity = lineDao.findById(id).get();

        assertAll(
                () -> assertThat(actualLineEntity.getUpwardTerminusId())
                        .isEqualTo(lineEntity.getUpwardTerminusId()),
                () -> assertThat(actualLineEntity.getDownwardTerminusId())
                        .isEqualTo(lineEntity.getDownwardTerminusId())
        );
    }

    @DisplayName("DB에서 모든 노선을 조회한다")
    @Test
    void shouldFindAllLinesWhenRequest() {
        LineEntity lineEntity1 = new LineEntity(
                1L,
                "2호선",
                1L,
                3L,
                0);
        SqlParameterSource params1 = new BeanPropertySqlParameterSource(lineEntity1);
        simpleJdbcInsert.executeAndReturnKey(params1).longValue();

        LineEntity lineEntity2 = new LineEntity(
                2L,
                "3호선",
                2L,
                3L,
                0);
        SqlParameterSource params2 = new BeanPropertySqlParameterSource(lineEntity2);
        simpleJdbcInsert.executeAndReturnKey(params2).longValue();

        List<LineEntity> actualLineEntities = lineDao.findAll();

        assertAll(
                () -> assertThat(actualLineEntities).hasSize(2),
                () -> assertThat(actualLineEntities.get(0).getUpwardTerminusId())
                        .isEqualTo(lineEntity1.getUpwardTerminusId()),
                () -> assertThat(actualLineEntities.get(0).getDownwardTerminusId())
                        .isEqualTo(lineEntity1.getDownwardTerminusId())
        );
    }

    @DisplayName("DB에 특정 노선을 업데이트한다.")
    @Test
    void shouldUpdateLineWhenRequest() {
        LineEntity lineEntity = new LineEntity(
                1L,
                "2호선",
                1L,
                3L,
                0);
        SqlParameterSource params = new BeanPropertySqlParameterSource(lineEntity);
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        LineEntity lineEntityToUpdate = new LineEntity(lineEntity.getId(), "2호선", 1L, 2L, 0);
        lineDao.update(lineEntityToUpdate);

        String sql = "SELECT id, name, upward_terminus_id, downward_terminus_id, fare FROM line WHERE id=:id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        LineEntity actualLineEntity = namedParameterJdbcTemplate.queryForObject(sql, sqlParameterSource, rowMapper);

        assertAll(
                () -> assertThat(actualLineEntity.getUpwardTerminusId())
                        .isEqualTo(lineEntityToUpdate.getUpwardTerminusId()),
                () -> assertThat(actualLineEntity.getDownwardTerminusId())
                        .isEqualTo(lineEntity.getDownwardTerminusId())
        );
    }
}
