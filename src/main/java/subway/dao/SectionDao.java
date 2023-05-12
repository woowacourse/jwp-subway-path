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
            new Line(rs.getLong("line_id"), rs.getString("line_name"), rs.getString("line_color")),
            rs.getInt("distance")
        );

    public SectionDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
            .withTableName("section")
            .usingGeneratedKeyColumns("id");
    }

    public List<Section> findAllByLineId(long lineId) {
        String sql = "SELECT se.id, station1.id AS up_station_id, station1.name AS up_station_name,"
            + " station2.id AS down_station_id, station2.name AS down_station_name,"
            + " l.id AS line_id, l.name AS line_name, l.color AS line_color, se.distance FROM section AS se"
            + " LEFT JOIN station AS station1 ON se.up_station_id = station1.id"
            + " LEFT JOIN station AS station2 ON se.down_station_id = station2.id"
            + " LEFT JOIN line AS l ON se.line_id = l.id"
            + " WHERE l.id = ?";
        return jdbcTemplate.query(sql, sectionRowMapper, lineId);
    }

    public void insertAll(List<Section> sections) {
        // TODO: bulk 처리로 수정
        for (Section section : sections) {
            insert(section);
        }
    }

    public void insert(Section section) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", section.getId());
        params.put("up_station_id", section.getUpStation().getId());
        params.put("down_station_id", section.getDownStation().getId());
        params.put("line_id", section.getLine().getId());
        params.put("distance", section.getDistance());

        insertAction.execute(params);
    }

    public void deleteAll(List<Section> sections) {
        for (Section section : sections) {
            deleteByUpStationIdAndDownStationId(section.getUpStation().getId(), section.getDownStation().getId());
        }
    }

    public void deleteByUpStationIdAndDownStationId(long upStationId, long downStationId) {
        String sql = "DELETE FROM section WHERE up_station_id = ? AND down_station_id = ?";
        jdbcTemplate.update(sql, upStationId, downStationId);
    }
}
