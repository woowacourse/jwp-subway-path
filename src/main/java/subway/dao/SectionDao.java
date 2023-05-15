package subway.dao;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Station;
import subway.entity.SectionEntity;


@Component
public class SectionDao {

    private static final RowMapper<Section> sectionRowMapper = (rs, rowNum) ->
            new Section(
                    new Station(rs.getLong("up_station_id"), rs.getString("up_station_name")),
                    new Station(rs.getLong("down_station_id"), rs.getString("down_station_name")),
                    new Distance(rs.getInt("distance"))
            );

    private static final RowMapper<SectionEntity> sectionEntityRowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("line_id"),
                    rs.getLong("up_station_id"),
                    rs.getLong("down_station_id"),
                    rs.getInt("distance")
            );

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public SectionDao(NamedParameterJdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("section");
    }

    public List<Section> findSectionsByLineId(Long lineId) {
        String sql = "SELECT up.id as up_station_id, up.name as up_station_name, down.id as down_station_id, down.name as down_station_name, s.distance "
                + "FROM SECTION AS s "
                + "JOIN STATION AS up ON s.up_station_id = up.id "
                + "JOIN STATION AS down ON s.down_station_id = down.id "
                + "WHERE s.line_id = :line_id";

        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("line_id", lineId);

        return jdbcTemplate.query(sql, source, sectionRowMapper);
    }

    //SectionEntity사용해야하면 바꿀 것
    public Optional<List<SectionEntity>> findByLineId(Long lineId) {
        String sql = "SELECT line_id, up_station_id, down_station_id, distance FROM SECTION WHERE line_id = :line_id";
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("line_id", lineId);
        try {
            return Optional.of(jdbcTemplate.query(sql, source, sectionEntityRowMapper));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<SectionEntity> findByUpStationId(Long upStationId, Long lineId) {
        String sql = "SELECT line_id, up_station_id, down_station_id, distance FROM SECTION WHERE line_id = :line_id AND up_station_id = :up_station_id";
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("up_station_id", upStationId)
                .addValue("line_id", lineId);
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, source, sectionEntityRowMapper));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<SectionEntity> findByDownStationId(Long downStationId, Long lineId) {
        String sql = "SELECT line_id, up_station_id, down_station_id, distance FROM SECTION WHERE line_id = :line_id AND down_station_id = :down_station_id";
        SqlParameterSource source = new MapSqlParameterSource()
                .addValue("down_station_id", downStationId)
                .addValue("line_id", lineId);
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, source, sectionEntityRowMapper));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    public void insert(SectionEntity sectionEntity) {
        SqlParameterSource source = new BeanPropertySqlParameterSource(sectionEntity);
        insertAction.execute(source);
    }

    public void delete(SectionEntity sectionEntity) {
        String sql = "DELETE FROM SECTION WHERE up_station_id = :upStationId AND down_station_id = :downStationId";

        SqlParameterSource source = new BeanPropertySqlParameterSource(sectionEntity);
        jdbcTemplate.update(sql, source);
    }

}
