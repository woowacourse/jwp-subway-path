package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("SECTIONS")
                .usingGeneratedKeyColumns("id");
    }

    public Sections findSectionsByLineId(final long lineId) {

        String sql = "SELECT SEC.uuid, S1.id, S1.name, S2.id, S2.name, SEC.distance"
                + " FROM SECTIONS AS SEC "
                + "INNER JOIN STATION AS S1 ON SEC.up_id = S1.id "
                + "INNER JOIN STATION AS S2 ON SEC.down_id = S2.id "
                + " WHERE SEC.line_id = ?";

        List<Section> sections = jdbcTemplate.query(sql, upStationMapper(), lineId);

        return new Sections(lineId, sections);
    }

    private RowMapper<Section> upStationMapper() {
        return (rs, rowNum) -> new Section(
                java.util.UUID.fromString(rs.getString(1)),
                new Station(rs.getLong(2), rs.getString(3)),
                new Station(rs.getLong(4), rs.getString(5)),
                rs.getInt(6));
    }

    public void save(List<Section> added, final long lineId) throws IllegalArgumentException {
        List<SqlParameterSource> entries = new ArrayList<>();

        if (added.isEmpty()) {return;}

        for (Section section : added) {
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                    .addValue("uuid", section.getId())
                    .addValue("up_id", section.getUpStation().getId())
                    .addValue("down_id", section.getDownStation().getId())
                    .addValue("distance", section.getDistance())
                    .addValue("line_id", lineId);

            entries.add(sqlParameterSource);
        }

        SqlParameterSource[] sources = entries.toArray(new SqlParameterSource[entries.size()]);
        simpleJdbcInsert.executeBatch(sources);
    }

    public void delete(List<Section> removed) {
        String sql = "delete from SECTIONS where uuid = ?";

        List<Object[]> removeIds = removed.stream()
                .map(section -> new Object[]{section.getId().toString()})
                .collect(Collectors.toList());

        jdbcTemplate.batchUpdate(sql, removeIds);
    }

    public List<Sections> findAllSections() {
        String findAllLine = "select id from LINE";

        List<Long> lineIds = jdbcTemplate.query(findAllLine, (rs, rowNum) -> {
            return rs.getLong("id");
        });

        List<Sections> sections = new ArrayList<>();
        for (Long lineId : lineIds) {
            sections.add(findSectionsByLineId(lineId));
        }
        return sections;
    }
}
