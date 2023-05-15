package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import subway.domain.*;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class SectionDao {
    public static final String LINE_ID = "line_id";
    public static final String CURRENT_STATION_ID = "current_station_id";
    public static final String NEXT_STATION_ID = "next_station_id";
    public static final String DISTANCE = "distance";
    public static final String ID = "id";
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final JdbcTemplate jdbcTemplate;
    private final LineDao lineDao;

    public SectionDao(LineDao lineDao, DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("SECTION")
                .usingGeneratedKeyColumns("ID");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.lineDao = lineDao;
    }

    public int countStations(Line line) {
        String sql = "select count(*) from SECTION where line_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, line.getId());
    }

    public boolean isLineEmpty(Line line) {
        return countStations(line) == 0;
    }

    public Section insert(Section section) {
        long id = insertAndReturnId(
                section.getLine().getId(),
                section.getPreviousStation().getId(),
                section.getNextStation().getId(),
                section.getDistance()
        );

        return new Section(id, section.getLine(), section.getPreviousStation(), section.getNextStation(), section.getDistance());
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

    public Optional<Section> findByPreviousStation(Station previousStation, Line line) {
        String sql = "select " +
                "SE.id as id, " +
                "PS.id as PS_ID, " +
                "PS.name as PS_NAME, " +
                "NS.id as NS_ID, " +
                "NS.name as NS_NAME, " +
                "L.id as L_ID, " +
                "L.name as L_NAME, " +
                "L.color as L_COLOR, " +
                "SE.distance as distance " +
                "from SECTION SE " +
                "inner join STATION PS " +
                "on SE.current_station_id = PS.id " +
                "left outer join STATION NS " +
                "on SE.next_station_id = NS.id " +
                "inner join LINE L " +
                "on SE.line_id = L.id " +
                "where SE.current_station_id = ? and SE.line_id = ?;";

        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, previousStation.getId(), line.getId());
        if (rs.next()) {
            return Optional.of(getSectionByRowSet(rs));
        }
        return Optional.empty();
    }

    public Optional<Section> findByNextStation(Station station, Line line) {
        String sql = "select " +
                "SE.id as id, " +
                "PS.id as PS_ID, " +
                "PS.name as PS_NAME, " +
                "NS.id as NS_ID, " +
                "NS.name as NS_NAME, " +
                "L.id as L_ID, " +
                "L.name as L_NAME, " +
                "L.color as L_COLOR, " +
                "SE.distance as distance " +
                "from SECTION SE " +
                "inner join STATION PS " +
                "on SE.current_station_id = PS.id " +
                "left outer join STATION NS " +
                "on SE.next_station_id = NS.id " +
                "inner join LINE L " +
                "on SE.line_id = L.id " +
                "where SE.next_station_id = ? and SE.line_id = ?;";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sql, station.getId(), line.getId());
        if (rs.next()) {
            return Optional.of(getSectionByRowSet(rs));
        }
        return Optional.empty();
    }

    public void deleteStation(Station station, Line line) {
        String sql = "delete from SECTION where current_station_id = ? and line_id = ?";

        if (countStations(line) == 2) {
            clearStations(line);
            return;
        }

        jdbcTemplate.update(sql, station.getId(), line.getId());
    }

    private void clearStations(Line line) {
        String sql = "delete from SECTION where line_id = ?";
        jdbcTemplate.update(sql, line.getId());
    }

    public List<Section> findAllByLine(Line line) {
        String sql = "select " +
                "SE.id as id, " +
                "PS.id as PS_ID, " +
                "PS.name as PS_NAME, " +
                "NS.id as NS_ID, " +
                "NS.name as NS_NAME, " +
                "L.id as L_ID, " +
                "L.name as L_NAME, " +
                "L.color as L_COLOR, " +
                "SE.distance as distance " +
                "from SECTION SE " +
                "inner join STATION PS " +
                "on SE.current_station_id = PS.id " +
                "left outer join STATION NS " +
                "on SE.next_station_id = NS.id " +
                "inner join LINE L " +
                "on SE.line_id = L.id " +
                "where SE.line_id = ?;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> getSectionByResultSet(rs), line.getId());
    }

    private static Section getSectionByResultSet(ResultSet rs) {
        try {
            var distance = rs.getInt("DISTANCE") == 0 ? new EmptyDistance() : Distance.of(rs.getInt("DISTANCE"));
            var nextStation = rs.getString("NS_NAME") == null ? new EmptyStation() : new Station(rs.getLong("NS_ID"), rs.getString("NS_NAME"));
            return new Section(rs.getLong("ID"),
                    new Line(rs.getLong("L_ID"), rs.getString("L_NAME"), rs.getString("L_COLOR")),
                    new Station(rs.getLong("PS_ID"), rs.getString("PS_NAME")),
                    nextStation,
                    distance);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Section getSectionByRowSet(SqlRowSet rs) {
        var distance = rs.getInt("DISTANCE") == 0 ? new EmptyDistance() : Distance.of(rs.getInt("DISTANCE"));
        var nextStation = rs.getString("NS_NAME") == null ? new EmptyStation() : new Station(rs.getLong("NS_ID"), rs.getString("NS_NAME"));
        return new Section(rs.getLong("ID"),
                new Line(rs.getLong("L_ID"), rs.getString("L_NAME"), rs.getString("L_COLOR")),
                new Station(rs.getLong("PS_ID"), rs.getString("PS_NAME")),
                nextStation,
                distance);
    }
}
