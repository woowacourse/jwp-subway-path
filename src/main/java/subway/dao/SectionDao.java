package subway.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<Section> sectionRowMapper = (rs, rowNum) ->
        new Section(
            rs.getLong("id"),
            new Station(rs.getLong("up_station_id"), rs.getString("up_station_name")),
            new Station(rs.getLong("down_station_id"), rs.getString("down_station_name")),
            new Line(rs.getLong("line_id"), rs.getString("line_name"), rs.getString("line_color"), rs.getInt("additional_charge")),
            rs.getInt("distance")
        );

    public SectionDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
            .withTableName("section")
            .usingGeneratedKeyColumns("id");
    }

    public List<Section> findAll() {
        String sql = "SELECT se.id, station1.id AS up_station_id, station1.name AS up_station_name,"
            + " station2.id AS down_station_id, station2.name AS down_station_name,"
            + " l.id AS line_id, l.name AS line_name, l.color AS line_color, l.additional_charge AS additional_charge,"
            + " se.distance FROM section AS se"
            + " LEFT JOIN station AS station1 ON se.up_station_id = station1.id"
            + " LEFT JOIN station AS station2 ON se.down_station_id = station2.id"
            + " LEFT JOIN line AS l ON se.line_id = l.id";
        return jdbcTemplate.query(sql, sectionRowMapper);
    }

    public List<Section> findAllByLineId(long lineId) {
        String sql = "SELECT se.id, station1.id AS up_station_id, station1.name AS up_station_name,"
            + " station2.id AS down_station_id, station2.name AS down_station_name,"
            + " l.id AS line_id, l.name AS line_name, l.color AS line_color, l.additional_charge AS additional_charge,"
            + " se.distance FROM section AS se"
            + " LEFT JOIN station AS station1 ON se.up_station_id = station1.id"
            + " LEFT JOIN station AS station2 ON se.down_station_id = station2.id"
            + " LEFT JOIN line AS l ON se.line_id = l.id"
            + " WHERE l.id = ?";
        return jdbcTemplate.query(sql, sectionRowMapper, lineId);
    }

    public List<Section> insertAll(List<Section> sections) {
        return sections.stream()
            .map(this::insert)
            .collect(Collectors.toList());
    }

    public Section insert(Section section) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", section.getId());
        params.put("up_station_id", section.getUpStation().getId());
        params.put("down_station_id", section.getDownStation().getId());
        params.put("line_id", section.getLine().getId());
        params.put("distance", section.getDistance());

        Long id = insertAction.executeAndReturnKey(params).longValue();
        return new Section(id, section.getUpStation(), section.getDownStation(), section.getLine(),
            section.getDistance());
    }

    public void deleteGivenSections(List<Section> sections) {
        for (Section section : sections) {
            deleteByUpStationIdAndDownStationId(section.getUpStation().getId(), section.getDownStation().getId());
        }
    }

    private void deleteByUpStationIdAndDownStationId(long upStationId, long downStationId) {
        String sql = "DELETE FROM section WHERE up_station_id = ? AND down_station_id = ?";
        jdbcTemplate.update(sql, upStationId, downStationId);
    }

    public void deleteByLineId(Long id) {
        String sql = "DELETE FROM section WHERE line_id = ?";
        jdbcTemplate.update(sql, id);
    }
}
