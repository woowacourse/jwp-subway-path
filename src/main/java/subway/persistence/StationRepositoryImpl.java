package subway.persistence;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import subway.domain.Station;
import subway.domain.repository.StationRepository;

@Repository
public class StationRepositoryImpl implements StationRepository {
	private static final int UPDATED_COUNT = 1;
	private final JdbcTemplate jdbcTemplate;
	private final SimpleJdbcInsert insert;

	private final RowMapper<Station> stationRowMapper = (rs, rowNum) ->
		new Station(
			rs.getLong("id"),
			rs.getString("name")
		);

	public StationRepositoryImpl(final JdbcTemplate jdbcTemplate) {
		this.insert = new SimpleJdbcInsert(jdbcTemplate)
			.withTableName("station")
			.usingGeneratedKeyColumns("id");
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public long createStation(final Station station) {
		SqlParameterSource params = new BeanPropertySqlParameterSource(station);
		return insert.executeAndReturnKey(params).longValue();
	}

	@Override
	public List<Station> findAll() {
		String sql = "select * from station";
		return jdbcTemplate.query(sql, stationRowMapper);
	}

	@Override
	public Station findById(final Long stationIdRequest) {
		String sql = "select * from station where id = ?";
		return jdbcTemplate.queryForObject(sql, stationRowMapper, stationIdRequest);
	}

	@Override
	public void updateStation(final long stationId, final Station station) {
		final String sql = "UPDATE station SET name = ? WHERE id = ?";
		final int updateCount = jdbcTemplate.update(sql, station.getName(), stationId);
		if (updateCount != UPDATED_COUNT) {
			throw new IllegalStateException(String.format("1개 이상의 상품이 수정되었습니다. 수정된 상품 수 : %d", updateCount));
		}
	}

	@Override
	public void deleteById(final Long stationIdRequest) {
		String sql = "delete from station where id = ?";
		jdbcTemplate.update(sql, stationIdRequest);
	}
}
