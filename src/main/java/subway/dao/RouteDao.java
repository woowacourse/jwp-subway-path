package subway.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.Route;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;

@Repository
public class RouteDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private static RowMapper<Section> SECTION_ROW_MAPPER = (rs, rowNum) ->
            new Section(
                    rs.getLong("line_id"),
                    new Station(rs.getLong("from_id"), rs.getString("from_name")),
                    new Station(rs.getLong("to_id"), rs.getString("to_name")),
                    rs.getInt("distance")
            );

    public RouteDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public Route findRoute(final List<Line> lines) {
        final String sql = "SELECT\n" +
                "    s.id AS section_id,\n" +
                "    s.from_id,\n" +
                "    f.name AS from_name,\n" +
                "    s.to_id,\n" +
                "    t.name AS to_name,\n" +
                "    s.distance,\n" +
                "    s.line_id\n" +
                "FROM\n" +
                "    SECTION s\n" +
                "    INNER JOIN STATION f ON s.from_id = f.id\n" +
                "    INNER JOIN STATION t ON s.to_id = t.id\n" +
                "    INNER JOIN LINE l ON s.line_id = l.id AND l.id=?;";

        final Map<Line, Sections> sectionsByLine = new HashMap<>();
        for (Line line : lines) {
            final Long id = line.getId();
            final Sections sections = new Sections(jdbcTemplate.query(sql, SECTION_ROW_MAPPER, id));
            sectionsByLine.put(line, sections);
        }
        return new Route(sectionsByLine);
    }

}
