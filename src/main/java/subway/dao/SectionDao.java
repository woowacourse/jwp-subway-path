package subway.dao;

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
import subway.dto.SectionRequest;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

//    private final RowMapper<List<Long>> sectionIdRowMapper = (rs, rowNum) ->
//            new ArrayList<>(
//                    List.of(rs.getLong("from_id"),
//                            rs.getLong("to_id"))
//            );

    private final RowMapper<Section> SECTION_ROW_MAPPER = (rs, rowNum) ->
            new Section(
                    new Station(rs.getLong("from_id"), rs.getString("from_name")),
                    new Station(rs.getLong("to_id"), rs.getString("to_name")),
                    rs.getInt("distance")
            );

    private final RowMapper<Section> SECTION_STATION_ROW_MAPPER = (rs, rowNum) ->
            new Section(
                    new Station(rs.getLong("from_id"), rs.getString("from_name")),
                    new Station(rs.getLong("to_id"), rs.getString("to_name")),
                    rs.getInt("distance")
            );

    private final RowMapper<Boolean> booleanMapper = (resultSet, rowNum) -> resultSet.getBoolean("isExist");

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

    public Sections findSectionsByLineId(final Long lineId) {
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

        return new Sections(jdbcTemplate.query(sql, SECTION_STATION_ROW_MAPPER, lineId));
    }

    public Integer count(final Long lineId, final Long stationId) {
        final String sql = "SELECT COUNT (*) FROM section WHERE (section.from_id = ? OR section.to_id = ?) AND section.line_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, stationId, stationId, lineId);
    }

//    public List<List<Long>> findAdjacentStationId(final Long lineId, final Long stationId) {
//        final String sql = "SELECT section.from_id, section.to_id FROM section WHERE (section.from_id =? OR section.to_id = ?)  AND section.line_id = ?";
//        return jdbcTemplate.query(sql, sectionIdRowMapper, stationId, stationId, lineId);
//    }

    public Long findLeftStationId(final Long lineId, final Long stationId) {
        final String sql = "SELECT section.from_id FROM section WHERE section.to_id = ?  AND section.line_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, stationId, lineId);
    }

    public Long findRightStationId(final Long lineId, final Long stationId) {
        final String sql = "SELECT section.to_id FROM section WHERE section.from_id = ? AND section.line_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, stationId, lineId);
    }

    public void deleteSectionByStationId(final Long lineId, final Long stationId) {
        final String sql = "DELETE FROM section WHERE line_id = ? and (section.from_id =? OR section.to_id = ?)";
        jdbcTemplate.update(sql, lineId, stationId, stationId);
    }

    public void deleteSectionBySectionInfo(final Long lineId, final Section section) {
        final String sql = "DELETE FROM section WHERE line_id = ? AND from_id = ? AND to_id = ?";
        jdbcTemplate.update(sql, lineId, section.getFrom().getId(), section.getTo().getId());
    }

    public Integer findLeftSectionDistance(final Long lineId, final Long stationId) {
        final String sql = "SELECT section.distance FROM section WHERE section.to_id = ? AND section.line_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, stationId, lineId);
    }

    public Integer findRightSectionDistance(final Long lineId, final Long stationId) {
        final String sql = "SELECT section.distance FROM section WHERE section.from_id = ? AND section.line_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, stationId, lineId);
    }

    public Sections findSectionsBySectionInfo(final Long lineId, final SectionRequest sectionRequest) {
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
                "    INNER JOIN LINE l ON s.line_id = l.id AND line_id=?\n" +
                "WHERE s.from_id IN (?, ?) OR s.to_id IN (?, ?);";

        return new Sections(jdbcTemplate.query(sql, SECTION_STATION_ROW_MAPPER,
                lineId,
                sectionRequest.getFromId(),
                sectionRequest.getToId(),
                sectionRequest.getFromId(),
                sectionRequest.getToId()));
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

    public Boolean isEmptyLine(Long lineId) {
        String sql = "SELECT EXISTS(SELECT * FROM section WHERE section.line_id = ?) as isExist";
        return jdbcTemplate.queryForObject(sql, booleanMapper, lineId);
    }
}
