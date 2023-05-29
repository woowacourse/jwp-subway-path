package subway.persistence.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import subway.persistence.entity.LineEntity;

@Repository
public class LineDao {

	private final JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert insertAction;
	private final RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
		new LineEntity(
			rs.getLong("id"),
			rs.getString("name"),
			rs.getString("color")
		);

	public LineDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
		this.jdbcTemplate = jdbcTemplate;
		this.insertAction = new SimpleJdbcInsert(dataSource)
			.withTableName("line")
			.usingGeneratedKeyColumns("id");
	}

	public LineEntity insert(final LineEntity line) {
		final Map<String, Object> params = new HashMap<>();
		params.put("id", line.getId());
		params.put("name", line.getName());
		params.put("color", line.getColor());

		final Long lineId = insertAction.executeAndReturnKey(params).longValue();
		return new LineEntity(lineId, line.getName(), line.getColor());
	}

	public List<LineEntity> findAll() {
		final String sql = "select id, name, color from LINE";
		return jdbcTemplate.query(sql, rowMapper);
	}

	public Optional<LineEntity> findById(final Long id) {
		final String sql = "select id, name, color from LINE WHERE id = ?";
		try {
			final LineEntity lineEntity = jdbcTemplate.queryForObject(sql, rowMapper, id);
			return Optional.ofNullable(lineEntity);
		} catch (EmptyResultDataAccessException e) {
			return Optional.empty();
		}
	}

	public void update(final LineEntity newLine) {
		final String sql = "update LINE set name = ?, color = ? where id = ?";
		jdbcTemplate.update(sql, newLine.getName(), newLine.getColor(), newLine.getId());
	}

	public void deleteById(final Long id) {
		final String sql = "delete from Line where id = ?";
		jdbcTemplate.update(sql, id);
	}
}
