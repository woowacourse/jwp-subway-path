package subway.persistence.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import subway.persistence.entity.StationEntity;

@Repository
public class StationDao {
	private final JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert insertAction;

	private final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
		new StationEntity(
			rs.getLong("id"),
			rs.getString("name")
		);

	public StationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
		this.jdbcTemplate = jdbcTemplate;
		this.insertAction = new SimpleJdbcInsert(dataSource)
			.withTableName("station")
			.usingGeneratedKeyColumns("id");
	}

	public StationEntity insert(StationEntity station) {
		final SqlParameterSource params = new BeanPropertySqlParameterSource(station);
		final Long id = insertAction.executeAndReturnKey(params).longValue();
		return new StationEntity(id, station.getName());
	}

	public List<StationEntity> findAll() {
		final String sql = "select * from STATION";
		return jdbcTemplate.query(sql, rowMapper);
	}

	public StationEntity findById(Long id) {
		final String sql = "select * from STATION where id = ?";
		return jdbcTemplate.queryForObject(sql, rowMapper, id);
	}

	public StationEntity findByName(final String name) {
		final String sql = "select * from STATION where name = ?";
		return jdbcTemplate.queryForObject(sql, rowMapper, name);
	}

	public void update(final StationEntity newStation) {
		final String sql = "update STATION set name = ? where id = ?";
		jdbcTemplate.update(sql, newStation.getName(), newStation.getId());
	}

	public void deleteById(Long id) {
		final String sql = "delete from STATION where id = ?";
		jdbcTemplate.update(sql, id);
	}
}
