package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import subway.entity.LineEntity;

import javax.sql.DataSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class LineDao {

	private final JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert insertAction;
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private final RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
		new LineEntity(
			rs.getLong("lineId"),
			rs.getString("name")
		);

	public LineDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource,
		final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.insertAction = new SimpleJdbcInsert(dataSource)
			.withTableName("line")
			.usingGeneratedKeyColumns("lineId");
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public long insert(final LineEntity lineEntity) {
		Map<String, Object> params = new HashMap<>();
		params.put("name", lineEntity.getName());

		return insertAction.executeAndReturnKey(params).longValue();
	}

	public List<LineEntity> findAll() {
		String sql = "SELECT lineId,  name FROM line";
		return jdbcTemplate.query(sql, rowMapper);
	}

	public LineEntity findByName(final String name) {
		String sql = "SELECT lineId,  name FROM line WHERE name = ?";
		return jdbcTemplate.queryForObject(sql, rowMapper, name);
	}

	public void deleteById(final Long id) {
		jdbcTemplate.update("DELETE FROM line WHERE lineId = ?", id);
	}

	public LineEntity findByLineName(final String lineName) {
		String sql = "SELECT lineId, name FROM line WHERE name = ?";
		return jdbcTemplate.queryForObject(sql, rowMapper, lineName);
	}

	public Optional<LineEntity> findById(final Long id) {
		String sql = "SELECT lineId, name FROM line WHERE lineId = :lineId";
		return namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource("lineId", id), rowMapper).stream()
			.findAny();
	}

	public void updateLine(final long lineId, final LineEntity lineEntity) {
		String sql = "UPDATE line SET name = ? WHERE lineId = ?";
		jdbcTemplate.update(sql, lineEntity.getName(), lineId);
	}
}
