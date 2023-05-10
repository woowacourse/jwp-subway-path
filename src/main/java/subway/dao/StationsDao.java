package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.Stations;

import javax.sql.DataSource;
import java.util.Optional;

@Repository
public class StationsDao {
    public static final String LINE_ID = "line_id";
    public static final String CURRENT_STATION_ID = "current_station_id";
    public static final String NEXT_STATION_ID = "next_station_id";
    public static final String DISTANCE = "distance";
    public static final String ID = "id";
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final JdbcTemplate jdbcTemplate;

    public StationsDao(DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("SUBWAY_MAP")
                .usingGeneratedKeyColumns("ID");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int countStations(Line line) {
        String sql = "select count(*) from SUBWAY_MAP where line_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, line.getId());
    }

    public long initialize(Stations stations) {
        if (countStations(stations.getLine()) != 0) {
            throw new IllegalArgumentException("이미 초기 설정이 완료된 노선입니다.");
        }

        long savedId = insertAndReturnId(
                stations.getLine().getId(),
                stations.getPreviousStation().getId(),
                stations.getNextStation().getId(),
                stations.getDistance()
        );

        insertAndReturnId(
                stations.getLine().getId(),
                stations.getNextStation().getId(),
                null,
                0
        );

        return savedId;
    }

    public Stations insert(Stations stations) {
        long id = insertAndReturnId(
                stations.getLine().getId(),
                stations.getPreviousStation().getId(),
                stations.getNextStation().getId(),
                stations.getDistance()
        );

        return Stations.builder()
                .id(id)
                .line(stations.getLine())
                .startingStation(stations.getPreviousStation())
                .before(stations.getNextStation())
                .distance(stations.getDistance()).build();
    }

    private long insertAndReturnId(Long id, Long previousStationId, Long nextStationId, int distance) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource
                .addValue(LINE_ID, id)
                .addValue(CURRENT_STATION_ID, previousStationId)
                .addValue(NEXT_STATION_ID, nextStationId)
                .addValue(DISTANCE, distance);
        return simpleJdbcInsert
                .executeAndReturnKey(parameterSource)
                .longValue();
    }

    public void update(Stations stations) {
        jdbcTemplate.update("update SUBWAY_MAP set next_station_id = ?, distance = ? where id = ?",
                stations.getNextStation().getId(),
                stations.getDistance(),
                stations.getId());
    }

    public boolean hasStation(Station station, Line line) {
        String sql = "select exists(select 1 from SUBWAY_MAP where current_station_id = ? and line_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, station.getId(), line.getId());
    }

    public int findDistanceBetween(Station stationA, Station stationB, Line line) {
        // 두 역이 current, next 나란히 있는 경우
        Optional<Stations> subwayMapOptional = findByPreviousStation(stationA, line);
        if (subwayMapOptional.isPresent() && subwayMapOptional.get().getNextStation().equals(stationB)) {
            return subwayMapOptional.get().getDistance();
        }

        // 두 역이 next, current 이렇게 나란히 있는 경우
        Optional<Stations> subwayMapOptional1 = findByNextStation(stationA, line);
        if (subwayMapOptional1.isPresent() && subwayMapOptional1.get().getPreviousStation().equals(stationB)) {
            return subwayMapOptional1.get().getDistance();
        }

        throw new IllegalArgumentException("주어진 두 역이 이웃한 역이 아닙니다.");
        // TODO: 아직까지 이웃하지 않은 역의 거리를 조회하지는 않는다고 가정합니다.
    }

    public Optional<Stations> findByPreviousStation(Station previousStation, Line line) {
        String sql = "select * from SUBWAY_MAP MAP " +
                "inner join STATION S " +
                "on MAP.next_station_id = S.id " +
                "where MAP.current_station_id = ? and MAP.line_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, previousStation.getId(), line.getId());
        if (rowSet.next()) {
            return Optional.of(Stations.builder()
                    .id(rowSet.getLong(ID))
                    .line(line)
                    .startingStation(previousStation)
                    .before(new Station(rowSet.getLong(NEXT_STATION_ID), rowSet.getString("name")))
                    .distance(rowSet.getInt(DISTANCE)).build());
        }
        return Optional.empty();
    }

    public Optional<Stations> findByNextStation(Station nextStation, Line line) {
        String sql = "select * from SUBWAY_MAP MAP " +
                "inner join STATION S " +
                "on MAP.current_station_id = S.id " +
                "where MAP.next_station_id = ? and MAP.line_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, nextStation.getId(), line.getId());
        if (rowSet.next()) {
            return Optional.of(Stations.builder()
                    .id(rowSet.getLong(ID))
                    .line(line)
                    .startingStation(new Station(rowSet.getLong(CURRENT_STATION_ID), rowSet.getString("name")))
                    .before(nextStation)
                    .distance(rowSet.getInt(DISTANCE)).build());
        }
        return Optional.empty();
    }

    public void deleteStation(Station station, Line line) {
        Optional<Stations> stationsToDeleteOptional = findByPreviousStation(station, line);
        Optional<Stations> stationsLeftOptional = findByNextStation(station, line);

        if (stationsToDeleteOptional.isPresent() && stationsLeftOptional.isPresent()) {
            Stations stationsToDelete = stationsToDeleteOptional.get();
            Stations stationsLeft = stationsLeftOptional.get();

            update(Stations.builder()
                    .id(stationsLeft.getId())
                    .startingStation(stationsLeft.getPreviousStation())
                    .before(stationsToDelete.getNextStation())
                    .distance(stationsToDelete.getDistance() + stationsLeft.getDistance())
                    .build());
        }

        String sql = "delete from SUBWAY_MAP where current_station_id = ? and line_id = ?";

        if (countStations(line) == 2) {
            clearStations(line);
            return;
        }

        jdbcTemplate.update(sql, station.getId(), line.getId());
    }

    private void clearStations(Line line) {
        String sql = "delete from SUBWAY_MAP where line_id = ?";
        jdbcTemplate.update(sql, line.getId());
    }
}
