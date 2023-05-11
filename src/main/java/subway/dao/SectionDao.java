package subway.dao;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import subway.domain.Distance;
import subway.domain.LineInfo;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.LineSection;

@Repository
public class SectionDao {

	private final JdbcTemplate jdbcTemplate;
	private final RowMapper<Section> sectionRowMapper = (rs, rowNum) ->
		new Section(
			rs.getLong("id"),
			new Station(rs.getLong("departure_station.id"), rs.getString("departure_station.name")),
			new Station(rs.getLong("arrival_station.id"), rs.getString("arrival_station.name")),
			new Distance(rs.getInt("section.distance")));

	private final RowMapper<LineSection> lineSectionRowMapper = (rs, rowNum) ->
		new LineSection(
			new LineInfo(rs.getLong("section.line_id"),
				rs.getString("line.name"),
				rs.getString("line.color")
			),
			new Section(rs.getLong("id"),
				new Station(rs.getLong("departure_station.id"), rs.getString("departure_station.name")),
				new Station(rs.getLong("arrival_station.id"), rs.getString("arrival_station.name")),
				new Distance(rs.getInt("section.distance"))
			)
		);

	public SectionDao(final JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Map<LineInfo, List<Section>> findSections() {
		String sql =
			"SELECT section.id, section.line_id, line.name, line.color,  departure_station.id, departure_station.name, arrival_station.id, arrival_station.name, section.distance "
				+ "FROM section "
				+ "LEFT JOIN station AS departure_station ON departure_station.id = section.departure_id "
				+ "LEFT JOIN station AS arrival_station ON arrival_station.id = section.arrival_id "
				+ "LEFT JOIN line ON line.id = section.line_id";

		final List<LineSection> lineSections = jdbcTemplate.query(sql, lineSectionRowMapper);

		return lineSections.stream()
			.collect(groupingBy(LineSection::getLineInfo, mapping(LineSection::getSection, toList())));
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
