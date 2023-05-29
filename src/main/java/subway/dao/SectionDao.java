package subway.dao;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<Section> SECTION_STATION_ROW_MAPPER = (rs, rowNum) -> {
        Optional<Station> fromStation = Optional.ofNullable(
                new Station(rs.getLong("from_id"), rs.getString("from_name"))
        );
        Optional<Station> toStation = Optional.ofNullable(
                new Station(rs.getLong("to_id"), rs.getString("to_name"))
        );

        int distance = rs.getInt("distance");

        if (fromStation.isPresent() && toStation.isPresent()) {
            return new Section(fromStation.get(), toStation.get(), distance);
        }
        return null;
    };


    public SectionDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(final Long fromId, final Long toId, final Integer distance, final Long lineId) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("from_id", fromId)
                .addValue("to_id", toId)
                .addValue("distance", distance)
                .addValue("line_id", lineId);

        return insertAction.executeAndReturnKey(params).longValue();
    }

    public Optional<Sections> findSectionsByLineId(final Long lineId) {
        final String sql = "SELECT\n" +
                "    s.id AS section_id,\n" +
                "    s.from_id,\n" +
                "    f.name AS from_name,\n" +
                "    s.to_id,\n" +
                "    t.name AS to_name,\n" +
                "    s.distance\n" +
                "FROM\n" +
                "    SECTION s\n" +
                "    INNER JOIN STATION f ON s.from_id = f.id\n" +
                "    INNER JOIN STATION t ON s.to_id = t.id\n" +
                "    INNER JOIN LINE l ON s.line_id = l.id AND line_id=?;";

        Optional<List<Section>> sectionResource = Optional.ofNullable(
                jdbcTemplate.query(sql, SECTION_STATION_ROW_MAPPER, lineId));
        if (sectionResource.get().isEmpty()) {
            return Optional.empty();
        }
        Sections sections = new Sections(sectionResource.get());
        return Optional.of(sections);
    }

    public void deleteSectionByStationId(final Long lineId, final Long stationId) {
        final String sql = "DELETE FROM section WHERE line_id = ? and (section.from_id =? OR section.to_id = ?)";
        jdbcTemplate.update(sql, lineId, stationId, stationId);
    }

    public void deleteSectionBySectionInfo(final Long lineId, final Section section) {
        final String sql = "DELETE FROM section WHERE line_id = ? AND from_id = ? AND to_id = ?";
        jdbcTemplate.update(sql, lineId, section.getFrom().getId(), section.getTo().getId());
    }


    public Sections findSectionsByStationInfo(final Long lineId, final Long stationId) {
        final String sql = "SELECT\n"
                + "    s.id AS section_id,\n"
                + "    s.from_id,\n"
                + "    f.name AS from_name,\n"
                + "    s.to_id,\n"
                + "    t.name AS to_name,\n"
                + "    s.distance\n"
                + "FROM\n"
                + "    SECTION s\n"
                + "    INNER JOIN STATION f ON s.from_id = f.id\n"
                + "    INNER JOIN STATION t ON s.to_id = t.id\n"
                + "WHERE\n"
                + "    s.line_id = ? AND (s.from_id = ? OR s.to_id = ?)";
        return new Sections(jdbcTemplate.query(sql, SECTION_STATION_ROW_MAPPER, lineId, stationId, stationId));
    }

    public Sections findAllSectionInfo() {
        final String sql = "SELECT ST.id from_id, ST.name from_name, ST2.id to_id, ST2.name to_name, distance\n"
                + "FROM SECTION SE\n"
                + "INNER JOIN STATION ST ON SE.from_id = ST.id \n"
                + "INNER JOIN STATION ST2 ON SE.to_id = ST2.id;";
        return new Sections(jdbcTemplate.query(sql, SECTION_STATION_ROW_MAPPER));
    }

}
