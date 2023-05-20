package subway.line.domain.section.infrastructure;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.line.domain.section.Section;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.section.domain.EmptyDistance;
import subway.line.domain.station.EmptyStation;
import subway.line.domain.station.Station;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SectionDao {
    public static final String LINE_ID = "line_id";
    public static final String CURRENT_STATION_ID = "current_station_id";
    public static final String NEXT_STATION_ID = "next_station_id";
    public static final String DISTANCE = "distance";
    public static final String ID = "id";
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final JdbcTemplate jdbcTemplate;

    public SectionDao(DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("SECTION")
                .usingGeneratedKeyColumns("ID");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Section insert(Long lineId, Station previousStation, Station nextStation, Distance distance) {
        long id = insertAndReturnId(
                lineId,
                previousStation.getId(),
                nextStation.getId(),
                distance
        );

        return new Section(id, previousStation, nextStation, distance);
    }

    private long insertAndReturnId(Long lineId, Long previousStationId, Long nextStationId, Distance distance) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource
                .addValue(LINE_ID, lineId)
                .addValue(CURRENT_STATION_ID, previousStationId)
                .addValue(NEXT_STATION_ID, nextStationId)
                .addValue(DISTANCE, distance.getValue());
        return simpleJdbcInsert
                .executeAndReturnKey(parameterSource)
                .longValue();
    }

    public void update(Section section) {
        jdbcTemplate.update("update SECTION set next_station_id = ?, distance = ? where id = ?",
                section.getNextStation().getId(),
                section.getDistance().getValue(),
                section.getId());
    }

    public void clearStations(Long lineId) {
        String sql = "delete from SECTION where line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }

    public List<Section> findAllByLineId(Long id) {
        String sql = "select " +
                "SE.id as id, " +
                "PS.id as PS_ID, " +
                "PS.name as PS_NAME, " +
                "NS.id as NS_ID, " +
                "NS.name as NS_NAME, " +
                "SE.distance as distance " +
                "from SECTION SE " +
                "inner join STATION PS " +
                "on SE.current_station_id = PS.id " +
                "left outer join STATION NS " +
                "on SE.next_station_id = NS.id " +
                "where SE.line_id = ?;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> getSectionByResultSet(rs), id);
    }

    private static Section getSectionByResultSet(ResultSet rs) {
        try {
            var distance = rs.getInt("DISTANCE") == 0 ? new EmptyDistance() : Distance.of(rs.getInt("DISTANCE"));
            var nextStation = rs.getString("NS_NAME") == null ? new EmptyStation() : new Station(rs.getLong("NS_ID"), rs.getString("NS_NAME"));
            return new Section(rs.getLong("ID"),
                    new Station(rs.getLong("PS_ID"), rs.getString("PS_NAME")),
                    nextStation,
                    distance);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    public void delete(Section section) {
        final var sql = "delete from SECTION where id = ?";
        jdbcTemplate.update(sql, section.getId());
    }
}
