package subway.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;

@Repository
public class SectionDao {

	private final JdbcTemplate jdbcTemplate;
	private final RowMapper<Section> sectionRowMapper = (rs, rowNum) ->
		new Section(
			rs.getLong("id"),
			new Station(rs.getLong("departure_station.id"), rs.getString("departure_station.name")),
			new Station(rs.getLong("arrival_station.id"), rs.getString("arrival_station.name")),
			new Distance(rs.getInt("section.distance")));

	public SectionDao(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Section> findSectionsByLineId(Long id) {
		String sql =
			"SELECT section.id, section.line_id, departure_station.id, departure_station.name, arrival_station.id, arrival_station.name, section.distance "
				+ "FROM section "
				+ "LEFT JOIN station AS departure_station ON departure_station.id = section.departure_id "
				+ "LEFT JOIN station AS arrival_station ON arrival_station.id = section.arrival_id "
				+ "WHERE section.line_id = ?";
		return jdbcTemplate.query(sql, sectionRowMapper, id);
	}

}
