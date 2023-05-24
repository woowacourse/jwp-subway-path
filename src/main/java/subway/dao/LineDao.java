package subway.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import subway.entity.LineEntity;

@Repository
public class LineDao {

	private final JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert insertAction;
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private final RowMapper<LineEntity> lineRowMapper = (rs, rowNum) ->
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

	public String insert(final LineEntity lineEntity) {
		Map<String, Object> params = new HashMap<>();
		params.put("name", lineEntity.getName());
		final long lineId = insertAction.executeAndReturnKey(params).longValue();

		return findById(lineId);
	}

	private String findById(final long lineId) {
		String sql = "SELECT * FROM line WHERE lineId = ?";
		final LineEntity lineEntity = jdbcTemplate.queryForObject(sql, lineRowMapper, lineId);

		return lineEntity.getName();
	}

	public List<LineEntity> findAll() {
		String sql = "SELECT lineId,  name FROM line";
		return jdbcTemplate.query(sql, lineRowMapper);
	}

	public LineEntity findByName(final String name) {
		String sql = "SELECT lineId,  name FROM line WHERE name = ?";
		return jdbcTemplate.queryForObject(sql, lineRowMapper, name);
	}

	public void deleteById(final Long id) {
		jdbcTemplate.update("DELETE FROM line WHERE lineId = ?", id);
	}

	public LineEntity findByLineName(final String lineName) {
		String sql = "SELECT lineId, name FROM line WHERE name = ?";
		return jdbcTemplate.queryForObject(sql, lineRowMapper, lineName);
	}

	public LineEntity findById(final Long id) {
		String sql = "SELECT * FROM line WHERE lineId = ?";
		return jdbcTemplate.queryForObject(sql, lineRowMapper, id);
	}

	public void updateLine(final long lineId, final LineEntity lineEntity) {
		String sql = "UPDATE line SET name = ? WHERE lineId = ?";
		jdbcTemplate.update(sql, lineEntity.getName(), lineId);
	}
}
