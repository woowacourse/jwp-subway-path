package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import subway.domain.vo.*;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.*;

@Repository
public class SectionDao {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Section> sectionRowMapper = (rs, rowNum) ->
            new Section(
                    rs.getLong("id"),
                    new Station(rs.getLong("departure_id"), rs.getString("departure_name")),
                    new Station(rs.getLong("arrival_id"), rs.getString("arrival_name")),
                    new Distance(rs.getInt("distance")));

    private final RowMapper<LineSection> lineSectionRowMapper = (rs, rowNum) ->
            new LineSection(
                    new Line(rs.getLong("sections.line_id"),
                            rs.getString("line.name"),
                            rs.getString("line.color")
                    ),
                    new Section(rs.getLong("id"),
                            new Station(rs.getLong("departure_id"), rs.getString("departure_name")),
                            new Station(rs.getLong("arrival_id"), rs.getString("arrival_name")),
                            new Distance(rs.getInt("distance"))
                    )
            );

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long saveSection(Long lineId, int distance, String departure, String arrival) {
        final String sql = "INSERT INTO sections (line_id, departure_id, arrival_id, distance) "
                + "SELECT ?, departure.id, arrival.id, ? "
                + "FROM station AS departure, station AS arrival "
                + "WHERE departure.name = ? AND arrival.name = ?";

        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, lineId);
            ps.setInt(2, distance);
            ps.setString(3, departure);
            ps.setString(4, arrival);
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public Map<Line, List<Section>> findSections() {
        String sql =
                "SELECT sections.id, sections.line_id, line.name,line.color, " +
                        "departure_station.id AS departure_id, departure_station.name AS departure_name, " +
                        "arrival_station.id AS arrival_id, arrival_station.name AS arrival_name, sections.distance AS distance "
                        + "FROM sections "
                        + "LEFT JOIN station AS departure_station ON departure_station.id = sections.departure_id "
                        + "LEFT JOIN station AS arrival_station ON arrival_station.id = sections.arrival_id "
                        + "LEFT JOIN line ON line.id = sections.line_id";

        final List<LineSection> lineSections = jdbcTemplate.query(sql, lineSectionRowMapper);

        return lineSections.stream()
                .collect(groupingBy(LineSection::getLineInfo, mapping(LineSection::getSection, toList())));
    }

    public List<Section> findAllSections() {
        String sql =
                "SELECT sections.id, sections.line_id, " +
                        "departure_station.id AS departure_id, departure_station.name AS departure_name, " +
                        "arrival_station.id AS arrival_id, arrival_station.name AS arrival_name, " +
                        "sections.distance AS distance "
                        + "FROM sections "
                        + "LEFT JOIN station AS departure_station ON departure_station.id = sections.departure_id "
                        + "LEFT JOIN station AS arrival_station ON arrival_station.id = sections.arrival_id ";
        return jdbcTemplate.query(sql, sectionRowMapper);
    }

    public List<Section> findSectionsByLineId(Long id) {
        String sql =
                "SELECT sections.id, sections.line_id, " +
                        "departure_station.id AS departure_id, departure_station.name AS departure_name, " +
                        "arrival_station.id AS arrival_id, arrival_station.name AS arrival_name, " +
                        "sections.distance AS distance "
                        + "FROM sections "
                        + "LEFT JOIN station AS departure_station ON departure_station.id = sections.departure_id "
                        + "LEFT JOIN station AS arrival_station ON arrival_station.id = sections.arrival_id "
                        + "WHERE sections.line_id = ?";
        return jdbcTemplate.query(sql, sectionRowMapper, id);
    }

    public RequestInclusiveSections findSectionByLineIdAndStationId(Long lineId, Long stationId) {
        String sql = "SELECT sections.id, sections.line_id, " +
                "departure_station.id AS departure_id, departure_station.name AS departure_name, " +
                "arrival_station.id AS arrival_id, arrival_station.name AS arrival_name, " +
                "sections.distance AS distance "
                + "FROM sections "
                + "LEFT JOIN station AS departure_station ON departure_station.id = sections.departure_id "
                + "LEFT JOIN station AS arrival_station ON arrival_station.id = sections.arrival_id "
                + "WHERE sections.line_id = ? AND sections.departure_id =? OR sections.arrival_id = ?";
        return new RequestInclusiveSections(jdbcTemplate.query(sql, sectionRowMapper
                , lineId, stationId, stationId));
    }

    public void deleteSection(Long sectionId) {
        final String sql = "DELETE FROM sections WHERE id = ?";

        jdbcTemplate.update(sql, sectionId);

    }
}
