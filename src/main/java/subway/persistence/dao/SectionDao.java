package subway.persistence.dao;


import java.util.List;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Distance;
import subway.domain.Station;
import subway.domain.section.Section;

@Repository
public class SectionDao {

    private static final RowMapper<Section> SECTION_ROW_MAPPER = (rs, num) -> new Section(
            rs.getLong("id"),
            new Station(rs.getLong("before_station_id"), rs.getString("before_station_name")),
            new Station(rs.getLong("next_station_id"), rs.getString("next_station_name")),
            new Distance(rs.getInt("distance"))
    );
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("SECTION")
                .usingGeneratedKeyColumns("id");
    }

    public void delete(final List<Section> sections) {
        final String sql = "delete from SECTION where id = ?";
        final List<Long> ids = sections.stream()
                .map(Section::getId)
                .collect(Collectors.toUnmodifiableList());
        jdbcTemplate.batchUpdate(sql, ids, ids.size(), (ps, argument) -> ps.setLong(1, argument));
    }

    public void insert(final Long lineId, final List<Section> sections) {
        final MapSqlParameterSource[] sources = sections.stream()
                .map(section -> new MapSqlParameterSource()
                        .addValue("BEFORE_STATION", section.getPrevStation().getId())
                        .addValue("NEXT_STATION", section.getNextStation().getId())
                        .addValue("DISTANCE", section.getDistance().getValue())
                        .addValue("LINE_ID", lineId)
                )
                .toArray(MapSqlParameterSource[]::new);
        simpleJdbcInsert.executeBatch(sources);
    }

    public List<Section> findByLineId(final Long lineId) {
        final String sql =
                "SELECT s.id, before_station.NAME AS before_station_name, before_station.id AS before_station_id, " +
                        "next_station.NAME AS next_station_name, next_station.id AS next_station_id, s.distance " +
                        "FROM SECTION s " +
                        "JOIN STATION before_station ON s.BEFORE_STATION = before_station.ID " +
                        "JOIN STATION next_station ON s.NEXT_STATION = next_station.ID " +
                        "WHERE s.LINE_ID = ?";
        return jdbcTemplate.query(sql, SECTION_ROW_MAPPER, lineId);
    }

    public List<Section> findAll() {
        final String sql =
                "SELECT s.id, before_station.NAME AS before_station_name, before_station.id AS before_station_id, " +
                        "next_station.NAME AS next_station_name, next_station.id AS next_station_id, s.distance " +
                        "FROM SECTION s " +
                        "JOIN STATION before_station ON s.BEFORE_STATION = before_station.ID " +
                        "JOIN STATION next_station ON s.NEXT_STATION = next_station.ID ";
        return jdbcTemplate.query(sql, SECTION_ROW_MAPPER);
    }
}
