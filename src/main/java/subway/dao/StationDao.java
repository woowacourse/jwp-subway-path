package subway.dao;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import subway.domain.subway.Station;
import subway.entity.StationEntity;

@Repository
public class StationDao {

	private final JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert insertAction;
	private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	private final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
		new StationEntity(
			rs.getLong("stationId"),
			rs.getString("name")
		);

	public StationDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource, final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.insertAction = new SimpleJdbcInsert(dataSource)
			.withTableName("station")
			.usingGeneratedKeyColumns("stationId");
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

	public long insert(final Station station) {
		SqlParameterSource params = new BeanPropertySqlParameterSource(station);
		return insertAction.executeAndReturnKey(params).longValue();
	}
}
